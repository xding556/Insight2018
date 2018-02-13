//
//  The AreaDonation object holds the collection of donations 
//  contributed from a specific area (zip code) and the collection
//  of donors in that area. 
//
//  The Donation object holds the donation data identified by the 
//  recipient, zip code and calendar year
//

import java.util.HashMap;

class AreaDonation {
    private static String SEPARATOR = "|";
    private static int percent;
    HashMap<String, String> donors;
    HashMap<String, Donation> donations;
	
    public AreaDonation(Contribution contribution, int percent){
	donors = new HashMap<String, String>();
	donations = new HashMap<String, Donation>();
	donors.put(contribution.donor, contribution.year);
	this.percent = percent;
    }
	
    public String recordDonation(Contribution contribution){
	Donation donation;
	String key = contribution.cmteID+SEPARATOR+contribution.year;
	if ( donations.containsKey(key)){
	    donation = donations.get(key);
	    donation.addDonation(contribution);
	} else {
	    donation =  new Donation(contribution);
	    donations.put(key, donation);
	}
	return donation.outputRow(contribution);
    }

    public boolean isRepeatDonor( Contribution contribution) {
	if ( donors.containsKey(contribution.donor)){
	    String priorYear = donors.get(contribution.donor);
	    if( contribution.year.compareTo(priorYear) > 0){
		return true;
	    } else if ( contribution.year.equals(priorYear)){
		return false;
	    } else {
		donors.put(contribution.donor, contribution.year);
		return false;
	    }
	} else {
	    donors.put(contribution.donor, contribution.year);
	    return false;
	}
    }

    private static class Donation {
	double totalAmount;
	int donationCount;
	PercentileHelper percentileHelper;

	public Donation(Contribution contribution) {
	    totalAmount = contribution.amount;
	    donationCount = 1;
	    percentileHelper = new PercentileHelper(percent);
	    percentileHelper.addNumber(contribution.amount);
	}

	public void addDonation(Contribution contribution){
	    totalAmount += contribution.amount;
	    donationCount++;
	    percentileHelper.addNumber(contribution.amount);
	}

	public void print(Contribution contribution) {
	    System.out.print( outputRow(contribution));
	}
	public String outputRow(Contribution contribution) {
	    return contribution.cmteID+SEPARATOR
		+ contribution.zipCode+SEPARATOR
		+ contribution.year+SEPARATOR
		+ Math.round(this.percentileHelper.getPercentile())+SEPARATOR
		+ Math.round(this.totalAmount)+SEPARATOR
		+ this.donationCount + "\n";
	}
    }
}

