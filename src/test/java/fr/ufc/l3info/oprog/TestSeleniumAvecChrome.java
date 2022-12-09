package fr.ufc.l3info.oprog;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import org.openqa.selenium.*;
import org.openqa.selenium.Dimension;
import org.openqa.selenium.Point;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.interactions.Actions;

import java.awt.*;
import java.util.concurrent.TimeUnit;

import static org.junit.Assert.assertTrue;


public class TestSeleniumAvecChrome {

  VeloAdapter velo;

  @Before
  public void setUp() {
    velo = new VeloAdapter();
    velo.startTest();
  }

  @After
  public void tearDown() {
    velo.endTest();
  }


  @Test
  public void testScenario1() {
    velo.selectStation(1);
    String c = velo.addCharacter();
    String bike = velo.borrowBike();
    velo.exitStation(c);
  }

  @Test
  public void testScenario2() {
    velo.selectStation(4);
    String c = velo.addCharacter();
    String bike = velo.borrowBike();
    //velo.moveInStation(0, 0);
    velo.exitStation(c);
    velo.selectStation(2);
    bike = velo.unBorrowBike();
    velo.removeCharacter(c);
  }

}

class VeloAdapter {

  /** Web driver used to send the commands **/
  private WebDriver driver;

  /**
   * Constructor for Metro Adapter.
   */
  public VeloAdapter() {
    System.setProperty("webdriver.chrome.driver", "/home/AD/agascagi/Téléchargements/chromedriver");
    ChromeOptions chromeOptions = new ChromeOptions();
    // chromeOptions.addArguments("--headless");
    driver = new ChromeDriver(chromeOptions);
    
    //System.setProperty("webdriver.gecko.driver", "/chemin/vers/le/geckodriver");
    //driver = new FirefoxDriver();


    // open browser window
    driver.manage().window().setPosition(new Point(0,0));
    java.awt.Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();
    driver.manage().window().setSize(new Dimension(dim.width, dim.height));
    driver.manage().timeouts().implicitlyWait(3, TimeUnit.SECONDS);

  }

  /**
   * Start a test by opening the URL.
   */
  public void startTest() {
    driver.get("https://fdadeau.github.io/velocity");
    wait(2000);
  }

  /**
   * Ends a test by closing the tab.
   */
  public void endTest() {
    driver.close();
    driver.quit();  // à enlever avec le plugin Firefox
  }


  /**
   * Selects a station from the map
   */
  public void selectStation(int i) {
    driver.findElement(By.cssSelector(".leaflet-marker-icon:nth-child(" + i + ")")).click();
    wait(2000);
    driver.findElement(By.cssSelector(".btnVoir")).click();
    wait(2000);
  }

  /**
   *  Adds a character to the scene.
   *  @return  the identifier of the new character.
   */
  public String addCharacter(){
    driver.findElement(By.id("btnAddCharacter")).click();
    wait(2000);
    return driver.findElement(By.cssSelector(".sprite.selected")).getAttribute("id");
  }

  public String borrowBike() {
    driver.findElement(By.cssSelector("#bornes .borne[data-velo]")).click();
    //Store the alert in a variable
    Alert confirm = null;
    boolean retry = true;
    do {
      wait(1000);
      try {
        confirm = driver.switchTo().alert();
        retry = false;
      }
      catch(Exception e) { }
    } while(retry);
    //Store the alert in a variable for reuse
    String text = confirm.getText();
    //Press the OK button
    confirm.accept();
    return text;
  }

  public String unBorrowBike() {
    driver.findElement(By.cssSelector("#bornes .borne:not([data-velo])")).click();
    //Store the alert in a variable
    Alert confirm = null;
    boolean retry = true;
    do {
      wait(1000);
      try {
        confirm = driver.switchTo().alert();
        retry = false;
      }
      catch(Exception e) { }
    } while(retry);
    //Store the alert in a variable for reuse
    String text = confirm.getText();
    //Press the OK button
    confirm.accept();
    return text;
  }

  public void moveInStation(int x, int y) {
    Actions buider = new Actions(driver);
    buider.moveToElement(driver.findElement(By.cssSelector("main")), x, y).click().build().perform();
    wait(5000);
  }


  /**
   * Removes the character from the scene.
   * @param id the identifier of the character to exit the scene.
   */
  public void removeCharacter(String id) {
    driver.findElement(By.id("btnExit")).click();
    wait(6000);
    try {
      driver.findElement(By.id(id));
      Assert.fail("Client should not exist anymore.");
    }
    catch (NoSuchElementException e) {
      // expected
    }
  }


  /**
   * Removes the character from the scene.
   * @param id the identifier of the character to exit the scene.
   */
  public void exitStation(String id) {
    driver.findElement(By.cssSelector(".btnExitStation")).click();
    wait(4000);
    assertTrue(driver.findElement(By.id("map")).isDisplayed());
  }

  /**
   * Utility function to pause the test execution.
   * (to wait for loading, or others actions to end)
   * @param ms the waiting time in milliseconds.
   */
  private static void wait(int ms) {
    try {
      Thread.sleep(ms);
    } catch (InterruptedException e) {
      Assert.fail("Test case interrupted");
    }

  }


}
