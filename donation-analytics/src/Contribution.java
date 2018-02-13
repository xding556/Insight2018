//
//  The Contribution object holds the contribution record/row data 
//  read from the input file
//

class Contribution {
    public String cmteID;
    public String zipCode;
    public String year;
    public String donor;
    public double amount;

    public Contribution(String cmteID, String zipCode, String year, String donor, double amount){
	this.cmteID = cmteID;
	this.zipCode = zipCode;
	this.donor = donor;
	this.year = year;
	this.amount = amount;
    }

    public String toString(String SEPARATOR){
	return cmteID+SEPARATOR
	    +zipCode+SEPARATOR
	    +year+SEPARATOR
	    +donor+SEPARATOR
	    +amount;
    }

    public static Contribution validateCreateContribution(String line){
	String[] words = line.split("\\|");
	if ( words[0].length() == 0 ) {
	    //System.out.println("Invalid CMTE_ID:"+words[0]);
	    return null;
	}
	if ( words[10].length() < 5) {
	    //System.out.println("Invalid ZIP_CODE:"+words[10]);
	    return null;
	}
	if ( words[7].length() == 0){
	    //System.out.println("Invalid NAME:"+words[7]);
	    return null;
	}
	if ( words[13].length() != 8 ) {
	    //System.out.println("Invalid TRANSACTION_DT:"+words[13]);
	    return null;
	} 
	double amount;
	try {
	    amount = Double.parseDouble(words[14]);
	} catch ( Exception e) {
	    //System.out.println("Invalid TRANSACTION_AMT:"+words[14]);
	    return null;
	}
	if ( words[15].length() != 0 ){
	    //System.out.println("Invalid OTHER_ID:"+words[15]);
	    return null;
	}
	
	return new Contribution(words[0], 
				words[10].substring(0, 5),  
				words[13].substring(words[13].length()-4, words[13].length()),
				words[7],
				amount );	 
    }
}
