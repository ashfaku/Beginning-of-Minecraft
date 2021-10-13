import java.util.ArrayList;

public class Cheese extends Food
{
	private String whichCheese;
	public Cheese(String name, int size, String which)
	{
		super(name, size);
		whichCheese = which;
	}
	public Cheese()
	{
		whichCheese = "None";
	}
	public String toString()
	{
		return super.toString() + " and is " + whichCheese;
	}
	public void eat()
	{
		System.out.println("eating fooooood!");
	}
	public void eatFoodTwo(String s)
	{
		super.eatFoodTwo(s);
	}
	public static void main(String[] args) 
	{
		Cheese cheese = new Cheese("Cheese", 10, "Rotten"), no = new Cheese();
		System.out.println(no);
		System.out.println(cheese);
		Object o = new Object();
		System.out.println(o);
		cheese.eat();
		cheese.eatFood();
		Food food = new Cheese();
		Food[] foods = new Food[10];
		for (int i = 0; i < foods.length; i++)
			foods[i] = new Cheese();
		ArrayList<Food> fooods = new ArrayList<Food>();
		fooods.add(new Cheese());
		fooods.add(new Cheese());
		int n = 5;
		recur(n);
		for (int i = 1; i < n; i++)
		{
			if (i == 2)
				System.out.println("no U");
			System.out.println(i);
		}
		String[] s = {"a", "b", "d", "zz"};
		recur(s, 3);
		ArrayList<String> ss = new ArrayList();
		for (String sss: s)
			ss.add(sss);
		recur(ss, 3);
	}
	public static void recur(ArrayList<String> strs, int n)
	{
		if (n > 0)
			recur(strs, n - 1);
		System.out.println(strs.get(n));
	}
	public static void recur(String[] strs, int n)
	{
		if (n > 0)
			recur(strs, n - 1);
		System.out.println(strs[n]);
	}
	public static void recur(int n)
	{
		if (n == 2)
			System.out.println("no U");
		if (n > 1)
			recur(n - 1);
		System.out.println(n);
	}
}
class Food
{
	private String name;
	private int size;
	public Food()
	{
		name = "food";
		size = 0;
	}
	public Food(String n, int s)
	{
		name = n;
		size = s;
	}
	public void eatFoodTwo(String s)
	{
		System.out.println(s + " cheese stuff ");
	}
	public void eatFood()
	{
		System.out.println("eating " + name);
	}
	public void eat()
	{
		System.out.println("eat food!");
	}
	public String toString()
	{
		return "Name: " + name + ", size: " + size;
	}
	private static int count = 0;
	public static int iterativeBinarySearch(int[] array, int low, int high, int target)
	  {
	    int mid;
	    int count = 0;
	    while(low <= high)
	    {
	      count++;
	      //find the middle
	      mid = (high + low)/2;
	      
	      //if the middle is less, search the right half.
	      if(array[mid]<target) low = mid + 1;
	      
	      //if it's more, search the left half.
	      else if(array[mid]>target) high = mid - 1;
	      
	      //goldilocks, baby bear, et cetera.
	      else 
	      {
	        System.out.println("iterative binary search count: " + count);
	        return mid;
	      }
	    }
	    //if you didn't find it, well...
	    System.out.println("iterative binary search count: " + count);
	    return -1;
	  }
	  
	  public static int recursiveBinarySearch(int[] arr, int low, int high, int target)
	  {
	    int mid;
	   //base case
	    if(low > high)
	    {
	      System.out.println("recursive binary search count: " + count);
	      return -1;
	    }
	    else
	    {
	      //find the middle
	      mid = (low + high)/2;
	      //if the value in the middle is less than the target...
	      if(arr[mid] < target)
	      {
	        count++;
	        //recursively call the right half of the array
	        return recursiveBinarySearch(arr, mid + 1, high, target);
	      }
	      //if it's greater...
	      if(arr[mid] > target)
	      {
	        count++;
	        //recursively call the left half of the array
	        return recursiveBinarySearch(arr, low, mid - 1, target);
	      }
	      count++;
	      System.out.println("recursive binary search count: " + count);
	      return mid;
	    }
	  }
	      
	  /**
	   * This method generates an array of length n
	   * that contains random digits.
	   */
	  public static int[] random(int n)
	  {
	    int[] arr = new int[n];
	    for(int i = 0; i < n; i++)
	    {
	      arr[i] = (int)(Math.random()*10);
	    }
	    return arr;
	  }
	  
	  public static void print(int[] arr)
	  {
	    for(int i: arr)
	      System.out.print(" " + i + " ");
	    System.out.println();
	  }
	  
	  public static int linearSearch(int[] arr, int target)
	  {
	    for(int i = 0; i < arr.length; i++)
	    {
	      if(arr[i]==target)
	      {
	        System.out.println("linear search count: " + (i + 1));
	        return i;
	      }
	    }
	    System.out.println("linear search count: " + arr.length);
	    return -1;
	  }
	  
	  public static void mergeSort(int[] arr, int low, int high)
	  {
	    //if the left index is less than right index
	    if(low < high)
	    {
	      //split it roughly in half
	      int mid = (low + high)/2;
	      
	      //recursively splitting the left in half
	      mergeSort(arr, low, mid);
	      
	      //then recursively split the right in half
	      mergeSort(arr, mid + 1, high);
	      
	      //then merge the subarrays in order
	      merge(arr, low, mid, high);
	    }
	  }
	  public static void merge(int[] arr, int low, int mid, int high)
	  {
	    //copy the left "half" to a new array
	    int[] left = new int[mid + 1 - low];
	    
	    //populate the copy
	    for(int i = 0; i < left.length; i++)
	      left[i] = arr[i + low];
	    
	    //keep track of the leftmost index
	    int leftIndex = 0;
	    
	    //as long as low < middle < high
	    while(low < mid + 1 && mid + 1 <= high)
	    {
	      
	      //if the right index is less than the left index
	      if(arr[mid+1] < left[leftIndex])
	      {
	        //store the right index and increment
	        arr[low] = arr[mid+1];
	        low++;
	        mid++;
	      }
	      //otherwise...
	      else
	      {
	        //take the left index and increment
	        arr[low] = left[leftIndex];
	        low++;
	        leftIndex++;
	      }
	    }
	    //there may be leftovers in the left "half"
	    while(low < mid + 1)
	    {
	      //so take them from the left "half"  and increment
	      arr[low] = left[leftIndex];
	      low++;
	      leftIndex++;
	    }
	  }
	      

	  public static void main(String[] args)
	  {
	    //get an array of random digits
	    int[] random = random(8);
	    
	    //print it out
	    print(random);
	    
	    //get a random digit to target
	    int target = (int)(Math.random()*10);
	    System.out.println("target digit: " + target);
	    
	    //use linear search to find it UNSORTED
	    int linearIndex = linearSearch(random, target);
	    
	    //print out that index
	    System.out.println("index: " + linearIndex);
	    
	    int index;
//	    index = recursiveBinarySearch(random, 0, random.length-1, target);
//	    System.out.println("index: " + index);
	    
	    //sort it, print it
	    mergeSort(random, 0, random.length-1);
	    print(random);
	    
	    //search it with iterative binary search, print the index.
	    index = iterativeBinarySearch(random, 0, random.length-1, target);
	    System.out.println("index: " + index);
	    
	    //search it with recursive binary search, print the index.
	    index = recursiveBinarySearch(random, 0, random.length-1, target);
	    System.out.println("index: " + index);
	    
	    //what about linear search of a sorted array?
	    index = linearSearch(random, target);
	    System.out.println("index: " + index);
	    
	  }
}
