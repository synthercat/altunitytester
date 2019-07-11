package ro.altom.altunitytester;

import com.sun.javafx.geom.Vec3f;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import java.io.IOException;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;

public class TestSampleScene5 {


    private static AltUnityDriver altUnityDriver;

    @BeforeClass
    public static void setUp() throws IOException {
        altUnityDriver = new AltUnityDriver("127.0.0.1", 13000);
    }

    @AfterClass
    public static void tearDown() throws Exception {
        altUnityDriver.stop();
    }

    @Before
    public void loadLevel() throws Exception {
        altUnityDriver.loadScene("Scene 1 AltUnityDriverTestScene");
    }

    @Test
    public void testGetCurrentScene() throws Exception {
        assertEquals("Scene 1 AltUnityDriverTestScene", altUnityDriver.getCurrentScene());
    }


    @Test
    public void TestMovementCube() throws InterruptedException {
        altUnityDriver.loadScene("Scene 5 Keyboard Input");


        AltUnityObject cube = altUnityDriver.findElement("Player1");
        Vec3f cubeInitialPostion = new Vec3f(cube.worldX, cube.worldY, cube.worldY);
        altUnityDriver.scrollMouse(30, 20);
        altUnityDriver.pressKey("K",,1 2);
        Thread.sleep(2000);
        cube = altUnityDriver.findElement("Player1");
        altUnityDriver.pressKeyAndWait("O",1, 1);

        Vec3f cubeFinalPosition = new Vec3f(cube.worldX, cube.worldY, cube.worldY);

        assertNotEquals(cubeInitialPostion, cubeFinalPosition);


    }

    @Test
    //Test Keyboard button press
    public void TestCameraMovement() throws InterruptedException {
        altUnityDriver.loadScene("Scene 5 Keyboard Input");


        AltUnityObject cube = altUnityDriver.findElement("Player1");
        Vec3f cubeInitialPostion = new Vec3f(cube.worldX, cube.worldY, cube.worldY);

        altUnityDriver.pressKey("W",1, 2);
        Thread.sleep(2000);
        cube = altUnityDriver.findElement("Player1");
        Vec3f cubeFinalPosition = new Vec3f(cube.worldX, cube.worldY, cube.worldY);

        assertNotEquals(cubeInitialPostion, cubeFinalPosition);

    }

    @Test
    //Testing mouse movement and clicking
    public void TestCreatingStars() throws InterruptedException {
        altUnityDriver.loadScene("Scene 5 Keyboard Input");

        AltUnityObject[] stars = altUnityDriver.findElementsWhereNameContains("Star");
        assertEquals(1, stars.length);

        altUnityDriver.moveMouse(800, 400, 1);
        Thread.sleep(1500);

        altUnityDriver.pressKey("Mouse0",1, 1);
        altUnityDriver.moveMouseAndWait(800, 200, 1);
        altUnityDriver.pressKeyAndWait("Mouse0",1, 1);


        stars = altUnityDriver.findElementsWhereNameContains("Star");
        assertEquals(4, stars.length);


    }
}
