//
// The DonationAnalytics object holds the collection of AreaDonation objects
// (identified by zip code) and method the process the contribution record 
// read from input file. This data pipeline works as follow:
//   1. Read row data from the specified input file
//   2. Validate the row data and create contribution record
//   3. Check if contributed from repeat donor, if so calculate the donation
//      data identified by combination of recipient, zip code and calendar year
//   4. Receive the output data and output the row to the specified output file
//

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Scanner;

class DonationAnalytics {
    HashMap<String, AreaDonation> areaDonations;
    int percent;

    DonationAnalytics(int percent){
	areaDonations = new HashMap<String, AreaDonation>();
	this.percent = percent;
    }
   
    public String processContribution(Contribution contribution) {
	String key = contribution.zipCode;
	if ( areaDonations.containsKey(key)){
	    AreaDonation areaDonation = areaDonations.get(key);
	    if (areaDonation.isRepeatDonor(contribution)){
		return areaDonation.recordDonation(contribution);
	    }
	} else {
	    areaDonations.put(key, new AreaDonation(contribution, percent));    
	}
	return null;
    }

    public static void main(String[] args ) {
	long start = System.currentTimeMillis();
	if ( args.length != 3) {
	    System.out.println("Usage: java DonationAnalytics <dataFile> <percentileFile> <outputFile>");
	    System.exit(1);
	}
	String dataFileName = args[0];
	String percentileFileName = args[1];
	String outputFileName = args[2];	
	try {
	    Scanner sc = new Scanner(new File(percentileFileName));
	    int percent = sc.nextInt();
	    DonationAnalytics analyzer = new DonationAnalytics(percent);
	    FileWriter fileWriter = new FileWriter(outputFileName);
	    BufferedReader bufferedReader = new BufferedReader(new FileReader(dataFileName));
	    String line = bufferedReader.readLine();
	    while ( line != null ) {
		Contribution contribution = Contribution.validateCreateContribution(line);
		if (contribution != null ){
		    String outputRow = analyzer.processContribution(contribution);
		    if ( outputRow != null ){
			fileWriter.write(outputRow);
		    }
		}
		line = bufferedReader.readLine();
	    }
	    fileWriter.flush();
	    fileWriter.close();
	    bufferedReader.close();
	} catch ( IOException e) {
            System.out.println("IOException: " + e.getMessage() + "\n"); 
	}
	long end = System.currentTimeMillis();
	double timeElapsed = (end - start) /1000.0;
	System.out.println("elapsedTime="+timeElapsed);
    }
}
