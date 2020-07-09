package com.aqa.tweet;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.testng.annotations.Test;

import twitter4j.Status;
import twitter4j.Twitter;
import twitter4j.TwitterException;
import twitter4j.TwitterFactory;


public class ApiTest{
	
	private long statusCreated;
	private String tweet="Este es mi tweet";
	private String randomTweet="";
	private Twitter twitter = TwitterFactory.getSingleton();
    
	@Test
	//get Timeline
    public void getTimeLine() throws TwitterException{			   
	    List<Status> statuses = twitter.getHomeTimeline();
	    for(Status sta:statuses) {
	    	// validate created_at field
	    	assertThat(sta.getCreatedAt()).isNotNull();;
	    	// validate retweetCount field
	    	assertThat(sta.getRetweetCount()).isNotNull();
	    	// validate text field
	    	assertThat(sta.getText()).isNotNull();	    	   
	    }
	    
	}
	
	@Test
	//create a tweet and update
    public void createTweet() throws TwitterException{
		Double random=getRandomIntegerBetweenRange(10,100);
		randomTweet=tweet+random.toString();
		
	    Status status = twitter.updateStatus(randomTweet);
	    statusCreated=status.getId();	    
	    //I could't find a method to update an existing tweet so I validate if the tweet was created correctly. 
	    assertThat(status.getText()).isEqualTo(randomTweet);	       
	}
	
	@Test
	//validate if the tweet is duplicated
    public void validateDuplicatedTweet() throws TwitterException{	
		
		try {
			Status status = twitter.updateStatus(randomTweet);
		}catch (TwitterException e) {
			assertThat(e.getErrorMessage()).isEqualTo("Status is a duplicate.");	
		}
	    	   	     
	}
	
	@Test(dependsOnMethods = "validateDuplicatedTweet")
	//delete tweet created in test 2
    public void deleteTweet() throws TwitterException{	
	    Status status = twitter.destroyStatus(statusCreated);	
	    
	    try {
	        Status removedStatus = twitter.showStatus(statusCreated);
	        if (removedStatus == null) { // 
	            
	        } else {
	            System.out.println("@" + status.getUser().getScreenName()
	                        + " - " + status.getText());
	        }
	        
	    } catch (TwitterException e) {
	    	assertThat(e.getErrorMessage()).isEqualTo("No status found with that ID.");	    	
	    }
	    
	}
	
	
	@Test(dependsOnMethods = "deleteTweet")
	public void verifyTwitter(){
		
		//please modify the chromedriver location
		System.setProperty("webdriver.chrome.driver", "C:\\Users\\Bpaternostro\\Documents\\Selenium\\drivers\\chromedriver.exe");
		WebDriver driver = new ChromeDriver();								
		driver.get("https://twitter.com/pater_bruno/status/"+statusCreated);
		
		TwitterPO t=new TwitterPO(driver);		
		assertThat(t.getMsj()).isEqualTo("Lo sentimos, esa página no existe.");
		driver.quit();
		
	}
	
	public static double getRandomIntegerBetweenRange(double min, double max){

	    double x = (int)(Math.random()*((max-min)+1))+min;

	    return x;

	}
 


}