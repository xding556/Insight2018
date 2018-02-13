//
//  The Percentile object uses the max heap and min heap to keep tracking 
//  the percentile while new number element is added to the collection
//

import java.util.PriorityQueue;

class PercentileHelper {
    PriorityQueue<Double> maxHeap = new PriorityQueue<Double>(
		  (Double v1, Double v2) -> { return v2>v1? 1:-1;});
    PriorityQueue<Double> minHeap = new PriorityQueue<Double>(
		  (Double v1, Double v2) -> { return v1>v2? 1:-1;});
    int percent;
    int numberCount;
	
    public PercentileHelper(int percent) {
	this.percent = percent;
    }
	
    private int getRank() {
	return (int)Math.ceil(numberCount*percent/100.0);
    }
	
    public void addNumber(double v) {
	numberCount++;
	if ( maxHeap.size()== 0 || v <= maxHeap.peek()) {
	    maxHeap.add(v);
	} else {
	    minHeap.add(v);
	}
	int rank = getRank();
	if ( maxHeap.size() > rank){
	    minHeap.add(maxHeap.poll());
	} else if ( maxHeap.size() < rank){
	    maxHeap.add(minHeap.poll());
	}
    }

    public double getPercentile(){
	return maxHeap.peek();
    }

    public void printHeap(){
	System.out.println(maxHeap + " " + minHeap + "  percentile="+getPercentile());
    }

}



