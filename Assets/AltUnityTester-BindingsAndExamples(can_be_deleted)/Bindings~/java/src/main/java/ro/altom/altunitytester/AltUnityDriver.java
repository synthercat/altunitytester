package ro.altom.altunitytester;

import ro.altom.altunitytester.Commands.AltCallStaticMethods;
import ro.altom.altunitytester.Commands.AltCallStaticMethodsParameters;
import ro.altom.altunitytester.Commands.AltStop;
import ro.altom.altunitytester.Commands.EnableLogging;
import ro.altom.altunitytester.Commands.FindObject.*;
import ro.altom.altunitytester.Commands.InputActions.*;
import ro.altom.altunitytester.Commands.OldFindObject.*;
import ro.altom.altunitytester.Commands.UnityCommand.*;
import ro.altom.altunitytester.altUnityTesterExceptions.*;

import java.io.*;
import java.net.Socket;

public class AltUnityDriver {

    private static final org.slf4j.Logger log = org.slf4j.LoggerFactory.getLogger(AltUnityDriver.class);
    public static class PlayerPrefsKeyType {
        public static int IntType = 1;
        public static int StringType = 2;
        public static int FloatType = 3;
    }
    public static final int READ_TIMEOUT = 30 * 1000;

    private Socket socket=null;
    private PrintWriter out=null;
    private DataInputStream in = null;

    private AltBaseSettings altBaseSettings;
    public AltUnityDriver(String ip, int port) {
        this(ip,port,";","&",false);
    }

    public AltUnityDriver(String ip, int port,String requestSeparator,String requestEnd) {
        this(ip,port,requestSeparator,requestEnd,false);
    }
    public AltUnityDriver(String ip,int port,String requestSeparator,String requestEnd,Boolean logEnabled){
        if (ip == null || ip.isEmpty()) {
            throw new InvalidParamerException("Provided IP address is null or empty");
        }
        try {
            log.info("Initializing connection to {}:{}", ip, port);
            socket = new Socket(ip, port);
            socket.setSoTimeout(READ_TIMEOUT);
            out = new PrintWriter(socket.getOutputStream(), true);
            in = new DataInputStream(socket.getInputStream());
        } catch (IOException e) {
            throw new ConnectionException("Could not create connection to " + String.format("%s:%d", ip, port), e);
        }
        altBaseSettings=new AltBaseSettings(socket,requestSeparator,requestEnd,out,in,logEnabled);
        EnableLogging();
    }
    private void EnableLogging(){
        new EnableLogging(altBaseSettings).Execute();
    }

    public void stop() {
        new AltStop(altBaseSettings).Execute();
    }

    public String callStaticMethods(AltCallStaticMethodsParameters altCallStaticMethodsParameters){
        return new AltCallStaticMethods(altBaseSettings,altCallStaticMethodsParameters).Execute();
    }

    

    public String callStaticMethods(String assembly, String typeName, String methodName,
                                    String parameters, String typeOfParameters) {
        AltCallStaticMethodsParameters altCallStaticMethodsParameters=new AltCallStaticMethodsParameters.Builder(typeName,methodName,parameters).withAssembly(assembly).withTypeOfParameters(typeOfParameters).build();
        return callStaticMethods(altCallStaticMethodsParameters);
    }

    public String callStaticMethods(String typeName, String methodName, String parameters) {
        return callStaticMethods("", typeName, methodName, parameters, "");
    }

    public void loadScene(String scene) {
        new AltLoadScene(altBaseSettings,scene).Execute();
    }

    public void deletePlayerPref() {
        new AltDeletePlayerPref(altBaseSettings).Execute();
    }

    public void deleteKeyPlayerPref(String keyName) {
        new AltDeleteKeyPlayerPref(altBaseSettings,keyName).Execute();
    }

    public void setKeyPlayerPref(String keyName, int valueName) {
        new AltSetKeyPlayerPref(altBaseSettings,keyName,valueName).Execute();
    }

    public void setKeyPlayerPref(String keyName, float valueName) {
        new AltSetKeyPlayerPref(altBaseSettings,keyName,valueName).Execute();
    }

    public void setKeyPlayerPref(String keyName, String valueName) {
        new AltSetKeyPlayerPref(altBaseSettings,keyName,valueName).Execute();
    }

    public int getIntKeyPlayerPref(String keyname) {
        return new AltIntGetKeyPlayerPref(altBaseSettings,keyname).Execute();
    }

    public float getFloatKeyPlayerPref(String keyname) {
        return new AltFloatGetKeyPlayerPref(altBaseSettings,keyname).Execute();
    }

    public String getStringKeyPlayerPref(String keyname) {
        return new AltStringGetKeyPlayerPref(altBaseSettings,keyname).Execute();
    }

    public String getCurrentScene() {
        return new AltGetCurrentScene(altBaseSettings).Execute();
    }

    public float getTimeScale() {
        return new AltGetTimeScale(altBaseSettings).Execute();
    }

    public void setTimeScale(float timeScale) {
        new AltSetTimeScale(altBaseSettings,timeScale).Execute();
    }

    public void swipe(int xStart, int yStart, int xEnd, int yEnd, float durationInSecs) {
        new AltSwipe(altBaseSettings,xStart,yStart,xEnd,yEnd,durationInSecs).Execute();
    }

    public void swipeAndWait(int xStart, int yStart, int xEnd, int yEnd, float durationInSecs) {
        new AltSwipeAndWait(altBaseSettings,xStart,yStart,xEnd,yEnd,durationInSecs).Execute();
    }

    public void holdButton(int xPosition, int yPosition, float durationInSecs) {
        swipe(xPosition, yPosition, xPosition, yPosition, durationInSecs);
    }

    public void holdButtonAndWait(int xPosition, int yPosition, float durationInSecs) {
        swipeAndWait(xPosition, yPosition, xPosition, yPosition, durationInSecs);
    }

    public AltUnityObject clickScreen(float x, float y) {
        return new AltClickScreen(altBaseSettings,x,y).Execute();
    }
    public void tilt(int x, int y, int z) {
        new AltTilt(altBaseSettings,x,y,z).Execute();
    }
    public void pressKey(AltPressKeyParameters altPressKeyParameters){
        new AltPressKey(altBaseSettings,altPressKeyParameters).Execute();
    }
    public void pressKey(String keyName,float power, float duration){
        AltPressKeyParameters altPressKeyParameters=new AltPressKeyParameters.Builder(keyName).withDuration(duration).withPower(power).build();
        pressKey(altPressKeyParameters);
    }
    public void pressKeyAndWait(AltPressKeyParameters altPressKeyParameters){
        new AltPressKeyAndWait(altBaseSettings,altPressKeyParameters).Execute();
    }
    public void pressKeyAndWait(String keyName,float power, float duration) {
        AltPressKeyParameters altPressKeyParameters=new AltPressKeyParameters.Builder(keyName).withPower(power).withDuration(duration).build();
        pressKeyAndWait(altPressKeyParameters);
    }
    public void moveMouse(AltMoveMouseParameters altMoveMouseParameters){
        new AltMoveMouse(altBaseSettings,altMoveMouseParameters).Execute();
    }
    public void moveMouse(int x,int y, float duration){
        AltMoveMouseParameters altMoveMouseParameters=new AltMoveMouseParameters.Builder(x, y).withDuration(duration).build();
        moveMouse(altMoveMouseParameters);
    }
    public void moveMouseAndWait(AltMoveMouseParameters altMoveMouseParameters){
        new AltMoveMouseAndWait(altBaseSettings,altMoveMouseParameters).Execute();
    }
    public void moveMouseAndWait(int x,int y, float duration) {
        AltMoveMouseParameters altMoveMouseParameters=new AltMoveMouseParameters.Builder(x,y).withDuration(duration).build();
        moveMouseAndWait(altMoveMouseParameters);
    }
    public void scrollMouse(AltScrollMouseParameters altScrollMouseParameters){
        new AltScrollMouse(altBaseSettings,altScrollMouseParameters).Execute();
    }
    public void scrollMouse(float speed, float duration){
        AltScrollMouseParameters altScrollMouseParameters=new AltScrollMouseParameters.Builder().withDuration(duration).withSpeed(speed).build();
        scrollMouse(altScrollMouseParameters);

    }
    public void scrollMouseAndWait(AltScrollMouseParameters altScrollMouseParameters){
        new AltScrollMouseAndWait(altBaseSettings,altScrollMouseParameters).Execute();
    }
    public void scrollMouseAndWait(float speed, float duration) {
        AltScrollMouseParameters altScrollMouseParameters=new AltScrollMouseParameters.Builder().withSpeed(speed).withDuration(duration).build();
        scrollMouseAndWait(altScrollMouseParameters);
    }
    public AltUnityObject findObject(AltFindObjectsParameters altFindObjectsParameters){
        return new AltFindObject(altBaseSettings,altFindObjectsParameters).Execute();

    }
    public AltUnityObject findObject(By by,String value,String cameraName,boolean enabled){
        AltFindObjectsParameters altFindObjectsParameters=new AltFindObjectsParameters.Builder(by,value).isEnabled(enabled).withCamera(cameraName).build();
        return findObject(altFindObjectsParameters);
    }
    public AltUnityObject findObject(By by,String value,boolean enabled){
        return findObject(by,value,"",enabled);
    }
    public AltUnityObject findObject(By by,String value,String cameraName){
        return findObject(by,value,cameraName,true);
    }
    public AltUnityObject findObject(By by,String value){
        return findObject(by,value,"",true);
    }

    public AltUnityObject findObjectWhichContains(AltFindObjectsParameters altFindObjectsParameters){
        return new AltFindObjectWhichContains(altBaseSettings,altFindObjectsParameters).Execute();
    }
    public AltUnityObject findObjectWhichContains(By by,String value,String cameraName,boolean enabled){
        AltFindObjectsParameters altFindObjectsParameters=new AltFindObjectsParameters.Builder(by,value).withCamera(cameraName).isEnabled(enabled).build();
        return findObjectWhichContains(altFindObjectsParameters);
    }
    public AltUnityObject findObjectWhichContains(By by,String value,boolean enabled){
        return findObjectWhichContains(by,value,"",enabled);
    }
    public AltUnityObject findObjectWhichContains(By by,String value,String cameraName){
        return findObjectWhichContains(by,value,cameraName,true);
    }
    public AltUnityObject findObjectWhichContains(By by,String value){
        return findObjectWhichContains(by,value,"",true);
    }
    public AltUnityObject[] findObjects(AltFindObjectsParameters altFindObjectsParameters){
        return new AltFindObjects(altBaseSettings,altFindObjectsParameters).Execute();
    }
    public AltUnityObject[] findObjects(By by,String value, String cameraName, boolean enabled) {
        AltFindObjectsParameters altFindObjectsParameters=new AltFindObjectsParameters.Builder(by,value).withCamera(cameraName).isEnabled(enabled).build();
        return findObjects(altFindObjectsParameters);
    }
    public AltUnityObject[] findObjects(By by,String value, String cameraName) {
        return findObjects(by,value,cameraName,true);
    }
    public AltUnityObject[] findObjects(By by,String value, boolean enabled) {
        return  findObjects(by,value,"",enabled);
    }
    public AltUnityObject[] findObjects(By by,String value) {
        return findObjects(by,value,"",true);
    }

    public AltUnityObject[] findObjectsWhichContains(AltFindObjectsParameters altFindObjectsParameters){
        return new AltFindObjectsWhichContains(altBaseSettings,altFindObjectsParameters).Execute();
    }

    public AltUnityObject[] findObjectsWhichContains(By by,String value, String cameraName, boolean enabled) {
        AltFindObjectsParameters altFindObjectsParameters=new AltFindObjectsParameters.Builder(by, value).withCamera(cameraName).isEnabled(enabled).build();
        return findObjectsWhichContains(altFindObjectsParameters);
    }
    public AltUnityObject[] findObjectsWhichContains(By by,String value, String cameraName) {
        return findObjectsWhichContains(by,value,cameraName,true);
    }
    public AltUnityObject[] findObjectsWhichContains(By by,String value, boolean enabled) {
        return  findObjectsWhichContains(by,value,"",enabled);
    }
    public AltUnityObject[] findObjectsWhichContains(By by,String value) {
        return findObjectsWhichContains(by,value,"",true);
    }

    @Deprecated
    public AltUnityObject findElementWhereNameContains(AltFindElementsParameters altFindElementsParameters) {
        return new AltFindElementWhereNameContains(altBaseSettings,altFindElementsParameters).Execute();

    }
    @Deprecated
    public AltUnityObject findElementWhereNameContains(String name, String cameraName,boolean enabled) {
        AltFindElementsParameters altFindElementsParameters=new AltFindElementsParameters.Builder(name).withCamera(cameraName).isEnabled(enabled).build();
        return findElementWhereNameContains(altFindElementsParameters);
    }

    @Deprecated
    public AltUnityObject findElementWhereNameContains(String name, String cameraName) {
        return findElementWhereNameContains(name, cameraName, true);
    }
    @Deprecated
    public AltUnityObject findElementWhereNameContains(String name, boolean enabled) {
        return findElementWhereNameContains(name, "", enabled);
    }
    @Deprecated
    public AltUnityObject findElementWhereNameContains(String name) {
        return findElementWhereNameContains(name, "");
    }

    public AltUnityObject[] getAllElements(AltGetAllElementsParameters altGetAllElementsParameters){
        return new AltGetAllElements(altBaseSettings,altGetAllElementsParameters).Execute();
    }
    public AltUnityObject[] getAllElements(String cameraName,boolean enabled) {
        AltGetAllElementsParameters altGetAllElementsParameters=new AltGetAllElementsParameters.Builder().withCamera(cameraName).isEnabled(enabled).build();
        return getAllElements(altGetAllElementsParameters);
    }

    public AltUnityObject[] getAllElements(String cameraName) {
        return getAllElements(cameraName,true);
    }

    public AltUnityObject[] getAllElements(boolean enabled)  {
        return getAllElements("",enabled);
    }

    public AltUnityObject[] getAllElements() throws Exception {
        return getAllElements("",true);
    }
    @Deprecated
    public AltUnityObject findElement(AltFindElementsParameters altFindElementsParameters){
        return new AltFindElement(altBaseSettings,altFindElementsParameters).Execute();
    }
    @Deprecated
    public AltUnityObject findElement(String name, String cameraName, boolean enabled) {
        AltFindElementsParameters altFindElementsParameters=new AltFindElementsParameters.Builder(name).isEnabled(enabled).withCamera(cameraName).build();
        return findElement(altFindElementsParameters);
    }
    @Deprecated
    public AltUnityObject findElement(String name,boolean enabled) {
        return findElement(name, "",enabled);
    }
    @Deprecated
    public AltUnityObject findElement(String name,String cameraName) {
        return findElement(name, cameraName,true);
    }
    @Deprecated
    public AltUnityObject findElement(String name) {
        return findElement(name, "",true);
    }

    public AltUnityObject[] findElements(AltFindElementsParameters altFindElementsParameters){
        return new AltFindElements(altBaseSettings,altFindElementsParameters).Execute();
    }
    @Deprecated
    public AltUnityObject[] findElements(String name, String cameraName, boolean enabled) {
        AltFindElementsParameters altFindElementsParameters=new AltFindElementsParameters.Builder(name).withCamera(cameraName).isEnabled(enabled).build();
        return findElements(altFindElementsParameters);
    }
    @Deprecated
    public AltUnityObject[] findElements(String name) {
        return findElements(name, "",true);
    }
    @Deprecated
    public AltUnityObject[] findElements(String name, String cameraName) {
        return findElements(name, cameraName,true);
    }
    @Deprecated
    public AltUnityObject[] findElements(String name, boolean enabled) {
        return findElements(name, "",enabled);
    }

    @Deprecated
    public AltUnityObject[] findElementsWhereNameContains(AltFindElementsParameters altFindElementsParameters){
        return new AltFindElementsWhereNameContains(altBaseSettings,altFindElementsParameters).Execute();
    }
    @Deprecated
    public AltUnityObject[] findElementsWhereNameContains(String name, String cameraName, boolean enabled) {
        AltFindElementsParameters altFindElementsParameters=new AltFindElementsParameters.Builder(name).withCamera(cameraName).isEnabled(enabled).build();
        return findElementsWhereNameContains(altFindElementsParameters);
    }
    @Deprecated
    public AltUnityObject[] findElementsWhereNameContains(String name, String cameraName) {
        return findElementsWhereNameContains(name,cameraName,true);
    }
    @Deprecated
    public AltUnityObject[] findElementsWhereNameContains(String name,boolean enabled) {
        return findElementsWhereNameContains(name,"",enabled);

    }
    @Deprecated
    public AltUnityObject[] findElementsWhereNameContains(String name) {
        return findElementsWhereNameContains(name,"",true);

    }
    public AltUnityObject tapScreen(int x, int y) {
        return new AltTapScreen(altBaseSettings,x,y).Execute();
    }

    public String waitForCurrentSceneToBe(AltWaitForCurrentSceneToBeParameters altWaitForCurrentSceneToBeParameters){
        return new AltWaitForCurrentSceneToBe(altBaseSettings,altWaitForCurrentSceneToBeParameters).Execute();
    }
    public String waitForCurrentSceneToBe(String sceneName, double timeout, double interval) {
        AltWaitForCurrentSceneToBeParameters altWaitForCurrentSceneToBeParameters=new AltWaitForCurrentSceneToBeParameters.Builder(sceneName).withInterval(interval).withTimeout(timeout).build();
        return  waitForCurrentSceneToBe(altWaitForCurrentSceneToBeParameters);
    }

    public String waitForCurrentSceneToBe(String sceneName) {
        return waitForCurrentSceneToBe(sceneName, 20, 0.5);
    }

    @Deprecated
    public AltUnityObject waitForElementWhereNameContains(AltWaitForElementParameters altWaitForElementParameters){
        return new AltWaitForElementWhereNameContains(altBaseSettings,altWaitForElementParameters).Execute();
    }
    @Deprecated
    public AltUnityObject waitForElementWhereNameContains(String name, String cameraName,boolean enabled, double timeout, double interval) {
        AltFindElementsParameters altFindElementsParameters=new AltFindElementsParameters.Builder(name).withCamera(cameraName).isEnabled(enabled).build();
        AltWaitForElementParameters altWaitForElementParameters=new AltWaitForElementParameters.Builder(altFindElementsParameters).withInterval(interval).withTimeout(timeout).build();
        return waitForElementWhereNameContains(altWaitForElementParameters);
    }
    @Deprecated
    public AltUnityObject waitForElementWhereNameContains(String name, String cameraName, double timeout, double interval) {
        AltFindElementsParameters altFindElementsParameters=new AltFindElementsParameters.Builder(name).withCamera(cameraName).build();
        AltWaitForElementParameters altWaitForElementParameters=new AltWaitForElementParameters.Builder(altFindElementsParameters).withInterval(interval).withTimeout(timeout).build();
        return waitForElementWhereNameContains(altWaitForElementParameters);
    }

    @Deprecated
    public AltUnityObject waitForElementWhereNameContains(String name) {
        return waitForElementWhereNameContains(name, "",true, 20, 0.5);
    }
    @Deprecated
    public AltUnityObject waitForElementWhereNameContains(String name,boolean enabled) {
        return waitForElementWhereNameContains(name, "",enabled, 20, 0.5);
    }
    @Deprecated
    public AltUnityObject waitForElementWhereNameContains(String name,String cameraName) {
        return waitForElementWhereNameContains(name, cameraName,true, 20, 0.5);
    }
    @Deprecated
    public void waitForElementToNotBePresent(AltWaitForElementParameters altWaitForElementParameters) {
        new AltWaitForElementToNotBePresent(altBaseSettings,altWaitForElementParameters).Execute();

    }
    @Deprecated
    public void waitForElementToNotBePresent(String name, String cameraName,boolean enabled, double timeout, double interval) {
        AltFindElementsParameters altFindElementsParameters=new AltFindElementsParameters.Builder(name).withCamera(cameraName).isEnabled(enabled).build();
        AltWaitForElementParameters altWaitForElementParameters=new AltWaitForElementParameters.Builder(altFindElementsParameters).withTimeout(timeout).withInterval(interval).build();
        waitForElementToNotBePresent(altWaitForElementParameters);
    }

    public AltUnityObject waitForObject(AltWaitForObjectsParameters altWaitForObjectsParameters){
        return new AltWaitForObject(altBaseSettings,altWaitForObjectsParameters).Execute();
    }
    public AltUnityObject waitForObject(By by,String value, String cameraName,boolean enabled, double timeout, double interval) {
        AltFindObjectsParameters altFindObjectsParameters=new AltFindObjectsParameters.Builder(by,value).withCamera(cameraName).isEnabled(enabled).build();
        AltWaitForObjectsParameters altWaitForObjectsParameters=new AltWaitForObjectsParameters.Builder(altFindObjectsParameters).withInterval(interval).withTimeout(timeout).build();
        return waitForObject(altWaitForObjectsParameters);
    }
    public AltUnityObject waitForObject(By by,String value) {
        return waitForObject(by,value,"",true,2,0.5);
    }
    public AltUnityObject waitForObjectWithText(AltWaitForObjectWithTextParameters altWaitForObjectWithTextParameters){
        return new AltWaitForObjectWithText(altBaseSettings,altWaitForObjectWithTextParameters).Execute();
    }
    public AltUnityObject waitForObjectWithText(By by,String value, String text, String cameraName,boolean enabled, double timeout, double interval) {
        AltFindObjectsParameters altFindElementsParameters=new AltFindObjectsParameters.Builder(by,value).isEnabled(enabled).withCamera(cameraName).build();
        AltWaitForObjectWithTextParameters altWaitForElementWithTextParameters=new AltWaitForObjectWithTextParameters.Builder(altFindElementsParameters,text).withInterval(interval).withTimeout(timeout).build();
        return waitForObjectWithText(altWaitForElementWithTextParameters);
    }
    public AltUnityObject waitForObjectWithText(By by,String value, String text) {
        return waitForObjectWithText(by,value,text,"",true,2,0.5);
    }

    public void waitForObjectToNotBePresent(AltWaitForObjectsParameters altWaitForObjectsParameters){
        new AltWaitForObjectToNotBePresent(altBaseSettings,altWaitForObjectsParameters).Execute();
    }
    public void waitForObjectToNotBePresent(By by,String value, String cameraName,boolean enabled, double timeout, double interval) {
        AltFindObjectsParameters altFindObjectsParameters=new AltFindObjectsParameters.Builder(by, value).withCamera(cameraName).isEnabled(enabled).build();
        AltWaitForObjectsParameters altWaitForObjectsParameters=new AltWaitForObjectsParameters.Builder(altFindObjectsParameters).withTimeout(timeout).withInterval(interval).build();
        waitForObjectToNotBePresent(altWaitForObjectsParameters);
    }
    public void waitForObjectToNotBePresent(By by,String value) {
        waitForObjectToNotBePresent(by,value,"",true,20,0.5);
    }

    public AltUnityObject waitForObjectWhichContains(AltWaitForObjectsParameters altWaitForObjectsParameters){
        return new AltWaitForObjectWhichContains(altBaseSettings,altWaitForObjectsParameters).Execute();
    }

    public AltUnityObject waitForObjectWhichContains(By by,String value, String cameraName,boolean enabled, double timeout, double interval) {
        AltFindObjectsParameters altFindObjectsParameters=new AltFindObjectsParameters.Builder(by,value).isEnabled(enabled).withCamera(cameraName).build();
        AltWaitForObjectsParameters altWaitForObjectsParameters=new AltWaitForObjectsParameters.Builder(altFindObjectsParameters).withInterval(interval).withTimeout(timeout).build();
        return waitForObjectWhichContains(altWaitForObjectsParameters);

    }

    public AltUnityObject waitForObjectWhichContains(By by,String value) {
        return waitForObjectWhichContains(by,value,"",true,30,0.5);
    }

    @Deprecated
    public void waitForElementToNotBePresent(String name) {
        waitForElementToNotBePresent(name, "",true, 20, 0.5);
    }
    @Deprecated
    public void waitForElementToNotBePresent(String name,String cameraName) {
        waitForElementToNotBePresent(name, cameraName,true, 20, 0.5);
    }
    @Deprecated
    public void waitForElementToNotBePresent(String name, boolean enabled) {
        waitForElementToNotBePresent(name, "",enabled, 20, 0.5);
    }

    @Deprecated
    public AltUnityObject waitForElement(AltWaitForElementParameters altWaitForElementParameters) {
        return new AltWaitForElement(altBaseSettings,altWaitForElementParameters).Execute();
    }
    @Deprecated
    public AltUnityObject waitForElement(String name, String cameraName,boolean enabled, double timeout, double interval) {
        AltFindElementsParameters altFindElementsParameters=new AltFindElementsParameters.Builder(name).withCamera(cameraName).isEnabled(enabled).build();
        AltWaitForElementParameters altWaitForElementParameters=new AltWaitForElementParameters.Builder(altFindElementsParameters).withTimeout(timeout).withInterval(interval).build();
        return waitForElement(altWaitForElementParameters);
    }

    @Deprecated
    public AltUnityObject waitForElement(String name) {
        return waitForElement(name, "",true, 20, 0.5);
    }
    @Deprecated
    public AltUnityObject waitForElement(String name,String cameraName) {
        return waitForElement(name, cameraName,true, 20, 0.5);
    }
    @Deprecated
    public AltUnityObject waitForElement(String name,boolean enabled) {
        return waitForElement(name, "",enabled, 20, 0.5);
    }
    @Deprecated
    public AltUnityObject waitForElementWithText(AltWaitForElementWithTextParameters altWaitForElementWithTextParameters) {
        return new AltWaitForElementWithText(altBaseSettings,altWaitForElementWithTextParameters).Execute();
    }
    @Deprecated
    public AltUnityObject waitForElementWithText(String name, String text, String cameraName,boolean enabled, double timeout, double interval) {
        AltFindElementsParameters altFindElementsParameters=new AltFindElementsParameters.Builder(name).withCamera(cameraName).isEnabled(enabled).build();
        AltWaitForElementWithTextParameters altWaitForElementWithTextParameters=new AltWaitForElementWithTextParameters.Builder(altFindElementsParameters,text).withInterval(interval).withTimeout(timeout).build();
        return waitForElementWithText(altWaitForElementWithTextParameters);
    }

    @Deprecated
    public AltUnityObject waitForElementWithText(String name, String text) {
        return waitForElementWithText(name, text, "",true, 20, 0.5);
    }
    @Deprecated
    public AltUnityObject waitForElementWithText(String name, String text,String cameraName) {
        return waitForElementWithText(name, text, cameraName,true, 20, 0.5);
    }
    @Deprecated
    public AltUnityObject waitForElementWithText(String name, String text,boolean enabled) {
        return waitForElementWithText(name, text, "",enabled, 20, 0.5);
    }

    @Deprecated
    public AltUnityObject findElementByComponent(AltFindElementsByComponentParameters altFindElementsByComponentParameters){
        return new AltFindElementByComponent(altBaseSettings,altFindElementsByComponentParameters).Execute();
    }

    @Deprecated
    public AltUnityObject findElementByComponent(String componentName,String assemblyName, String cameraName,boolean enabled) {
        AltFindElementsByComponentParameters altFindElementsByComponentParameters=new AltFindElementsByComponentParameters.Builder(componentName).inAssembly(assemblyName).isEnabled(enabled).withCamera(cameraName).build();
        return findElementByComponent(altFindElementsByComponentParameters);
    }

    @Deprecated
    public AltUnityObject findElementByComponent(String componentName) {
        return findElementByComponent(componentName, "","",true);
    }

    @Deprecated
    public AltUnityObject findElementByComponent(String componentName, String cameraName) {
        return findElementByComponent(componentName,"", cameraName,true);
    }

    @Deprecated
    public AltUnityObject findElementByComponent(String componentName,String assemblyName, boolean enabled) {
        return findElementByComponent(componentName, assemblyName,"", enabled);
    }

    @Deprecated
    public AltUnityObject[] findElementsByComponent(AltFindElementsByComponentParameters altFindElementsByComponentParameters){
        return new AltFindElementsByComponent(altBaseSettings,altFindElementsByComponentParameters).Execute();
    }
    @Deprecated
    public AltUnityObject[] findElementsByComponent(String componentName, String assemblyName, String cameraName, boolean enabled) {
        AltFindElementsByComponentParameters altFindElementsByComponentParameters=new AltFindElementsByComponentParameters.Builder(componentName).inAssembly(assemblyName).isEnabled(enabled).withCamera(cameraName).build();
        return  findElementsByComponent(altFindElementsByComponentParameters);
    }

    @Deprecated
    public AltUnityObject[] findElementsByComponent(String componentName,String assemblyName ) {
        return findElementsByComponent(componentName, assemblyName,"",true);
    }

    @Deprecated
    public AltUnityObject[] findElementsByComponent(String componentName,String assemblyName, boolean enabled) {
        return findElementsByComponent(componentName, assemblyName,"", enabled);
    }

    // TODO: move those two out of this type and make them compulsory
    public static void setupPortForwarding(String platform,String deviceID, int local_tcp_port, int remote_tcp_port) {
        log.info("Setting up port forward for " + platform + " on port " + remote_tcp_port);
        removePortForwarding();
        if (platform.toLowerCase().equals("android".toLowerCase())) {
            try {
                String commandToRun;
                if(deviceID.equals(""))
                    commandToRun = "adb forward tcp:" + local_tcp_port + " tcp:" + remote_tcp_port;
                else
                    commandToRun = "adb -s "+deviceID+" forward  tcp:" + local_tcp_port + " tcp:" + remote_tcp_port;
                Runtime.getRuntime().exec(commandToRun);
                Thread.sleep(1000);
                log.info("adb forward enabled.");
            } catch (Exception e) {
                log.warn("AltUnityServer - abd probably not installed\n" + e);
            }

        } else if (platform.toLowerCase().equals("ios".toLowerCase())) {
            try {
                String commandToRun;
                if(deviceID.equals(""))
                    commandToRun = "iproxy " + local_tcp_port + " " + remote_tcp_port + "&";
                else
                    commandToRun = "iproxy " + local_tcp_port + " " + remote_tcp_port+" "+deviceID  + "&";
                Runtime.getRuntime().exec(commandToRun);
                Thread.sleep(1000);
                log.info("iproxy forward enabled.");
            } catch (Exception e) {
                log.warn("AltUnityServer - no iproxy process was running/present\n" + e);
            }
        }
    }

    public static void removePortForwarding() {
        try {
            String commandToExecute = "killall iproxy";
            Runtime.getRuntime().exec(commandToExecute);
            Thread.sleep(1000);
            log.info("Killed any iproxy process that may have been running...");
        } catch (Exception e) {
            log.warn("AltUnityServer - no iproxy process was running/present\n" + e);
        }

        try {
            String commandToExecute = "adb forward --remove-all";
            Runtime.getRuntime().exec(commandToExecute);
            Thread.sleep(1000);
            log.info("Removed existing adb forwarding...");
        } catch (Exception e) {
            log.warn("AltUnityServer - adb probably not installed\n" + e);
        }
    }
    public enum By
    {
        TAG,LAYER,NAME,COMPONENT,PATH,ID
    }

}
