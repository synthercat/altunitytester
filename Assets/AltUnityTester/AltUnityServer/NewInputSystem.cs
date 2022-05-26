#if ENABLE_INPUT_SYSTEM
using System.Collections;
using System.Collections.Generic;
using Altom.AltUnityTester;
#if USE_INPUT_SYSTEM_1_3
using NUnit.Framework;
using NUnit.Framework.Interfaces;
using NUnit.Framework.Internal;
#endif
using UnityEngine;
using UnityEngine.InputSystem;
using UnityEngine.InputSystem.Controls;

namespace Altom.AltUnityTester
{
    public class NewInputSystem : MonoBehaviour
    {
        private static Vector2 endTouchScreenPos;
        private static float keyDownPower;

        public static InputTestFixture InputTestFixture = new InputTestFixture();
        public static NewInputSystem Instance;
        public static Keyboard Keyboard;
        public static Mouse Mouse;
        public static Gamepad Gamepad;
        public static Touchscreen Touchscreen;
        public static Accelerometer Accelerometer;
        public static bool[] touches = new bool[] { false, true, true, true, true, true, true, true, true, true, true };
        public void Awake()
        {
            if (Instance == null)
                Instance = this;
            InputTestFixture = new InputTestFixture();
#if USE_INPUT_SYSTEM_1_3
            TestExecutionContext testExecutionContext = new TestExecutionContext();
            IMethodInfo methodInfo = new MethodWrapper(typeof(TestExample), typeof(TestExample).GetMethod("Test"));
            testExecutionContext.CurrentTest = new TestMethod(methodInfo);
            TestContext testContext = new TestContext(testExecutionContext);
            TestContext.CurrentTestExecutionContext = testExecutionContext;
            Application.runInBackground = true;
            InputSystem.settings.backgroundBehavior = InputSettings.BackgroundBehavior.IgnoreFocus;
            InputSystem.settings.editorInputBehaviorInPlayMode = InputSettings.EditorInputBehaviorInPlayMode.AllDeviceInputAlwaysGoesToGameView;
#endif

            Keyboard = (Keyboard)InputSystem.GetDevice("AltUnityKeyboard");
            if (Keyboard == null)
            {
                Keyboard = InputSystem.AddDevice<Keyboard>("AltUnityKeyboard");
            }

            Mouse = (Mouse)InputSystem.GetDevice("AltUnityMouse");
            if (Mouse == null)
            {
                Mouse = InputSystem.AddDevice<Mouse>("AltUnityMouse");

            }
            Gamepad = (Gamepad)InputSystem.GetDevice("AltUnityGamepad");
            if (Gamepad == null)
            {
                Gamepad = InputSystem.AddDevice<Gamepad>("AltUnityGamepad");

            }
            Touchscreen = (Touchscreen)InputSystem.GetDevice("AltUnityTouchscreen");
            if (Touchscreen == null)
            {
                Touchscreen = InputSystem.AddDevice<Touchscreen>("AltUnityTouchscreen");
            }
            Accelerometer = (Accelerometer)InputSystem.GetDevice("AltUnityAccelerometer");
            if (Accelerometer == null)
            {
                Accelerometer = InputSystem.AddDevice<Accelerometer>("AltUnityAccelerometer");
            }
            InputTestFixture.Set(Mouse.position, new Vector2(0, 0));


        }

        public static void DisableDefaultDevicesAndEnableAltUnityDevices()
        {
            foreach (var device in InputSystem.devices)
            {
                if (device.name.Contains("AltUnity"))
                {
                    InputSystem.EnableDevice(device);
                }
                else
                {
                    InputSystem.DisableDevice(device);

                }
            }

        }
        public static void EnableDefaultDevicesAndDisableAltUnityDevices()
        {
            foreach (var device in InputSystem.devices)
            {
                if (device.name.Contains("AltUnity"))
                {
                    InputSystem.DisableDevice(device);
                }
                else
                {
                    InputSystem.EnableDevice(device);

                }
            }

        }

        internal static IEnumerator ScrollLifeCycle(float speedVertical, float speedHorizontal, float duration)
        {
            float currentTime = 0;
            while (currentTime <= duration)
            {
                InputTestFixture.Set(Mouse.scroll, new Vector2(speedHorizontal * Time.unscaledDeltaTime / duration, speedVertical * Time.unscaledDeltaTime / duration), queueEventOnly: true);
                yield return null;
                currentTime += Time.unscaledDeltaTime;
            }
            InputTestFixture.Set(Mouse.scroll, new Vector2(0, 0), queueEventOnly: true);
        }

        internal static IEnumerator MoveMouseCycle(UnityEngine.Vector2 location, float duration)
        {
            float time = 0;
            yield return null;
            var mousePosition = new Vector2(Mouse.position.x.ReadValue(), Mouse.position.y.ReadValue());
            var distance = location - new UnityEngine.Vector2(mousePosition.x, mousePosition.y);

            var deltaUnchanged = false;
            while (time < duration)
            {
                UnityEngine.Vector2 delta;
                if (time + UnityEngine.Time.unscaledDeltaTime < duration)
                {
                    delta = distance * UnityEngine.Time.unscaledDeltaTime / duration;
                }
                else
                {
                    delta = location - new UnityEngine.Vector2(mousePosition.x, mousePosition.y);
                }

                mousePosition += delta;
                if (delta == Vector2.zero)
                {
                    deltaUnchanged = true;
                    break;
                }
                InputTestFixture.Move(Mouse.position, mousePosition, delta);
                yield return null;
                time += UnityEngine.Time.unscaledDeltaTime;
            }
            if (deltaUnchanged)
            {
                InputTestFixture.Move(Mouse.position, mousePosition * 1.01f, Vector2.zero);
                InputTestFixture.Move(Mouse.position, mousePosition, Vector2.zero);
                yield return new WaitForSecondsRealtime(duration - time);
            }
            InputTestFixture.Set(Mouse.position, mousePosition);

        }


        internal static IEnumerator TapElementCycle(GameObject target, int count, float interval)
        {
            Touchscreen.MakeCurrent();
            var touchId = getFreeTouch(touches);
            touches[touchId] = false;
            UnityEngine.Vector3 screenPosition;
            AltUnityRunner._altUnityRunner.FindCameraThatSeesObject(target, out screenPosition);
            for (int i = 0; i < count; i++)
            {
                float time = 0;
                InputTestFixture.BeginTouch(touchId, screenPosition, screen:Touchscreen);
                yield return null;
                time += Time.unscaledDeltaTime;
                InputTestFixture.EndTouch(touchId, screenPosition, screen:Touchscreen);
                if (i != count - 1 && time < interval)
                    yield return new WaitForSecondsRealtime(interval - time);

            }
            touches[touchId] = true;
        }
        internal static IEnumerator TapCoordinatesCycle(UnityEngine.Vector2 screenPosition, int count, float interval)
        {
            Touchscreen.MakeCurrent();
            var touchId = getFreeTouch(touches);
            touches[touchId] = false;
            for (int i = 0; i < count; i++)
            {
                float time = 0;
                InputTestFixture.BeginTouch(touchId, screenPosition, screen:Touchscreen);
                yield return null;
                time += Time.unscaledDeltaTime;
                endTouchScreenPos = screenPosition;
                InputTestFixture.EndTouch(touchId, screenPosition, screen:Touchscreen);
                if (i != count - 1 && time < interval)
                    yield return new WaitForSecondsRealtime(interval - time);
            }
            touches[touchId] = true;
        }

        internal static IEnumerator ClickElementLifeCycle(GameObject target, int count, float interval)
        {
            Mouse.MakeCurrent();
            UnityEngine.Vector3 screenPosition;
            AltUnityRunner._altUnityRunner.FindCameraThatSeesObject(target, out screenPosition);
            InputTestFixture.Set(Mouse.position, screenPosition, queueEventOnly: true);
            for (int i = 0; i < count; i++)
            {
                float time = 0;
                InputTestFixture.Press(Mouse.leftButton, queueEventOnly: true);
                yield return null;
                time += Time.unscaledDeltaTime;
                InputTestFixture.Release(Mouse.leftButton, queueEventOnly: true);
                if (i != count - 1 && time < interval)
                    yield return new WaitForSecondsRealtime(interval - time);
            }
        }
        internal static IEnumerator ClickCoordinatesLifeCycle(UnityEngine.Vector2 screenPosition, int count, float interval)
        {
            Mouse.MakeCurrent();
            InputTestFixture.Set(Mouse.position, screenPosition, queueEventOnly: true);
            for (int i = 0; i < count; i++)
            {
                float time = 0;
                InputTestFixture.Press(Mouse.leftButton, queueEventOnly: true);
                yield return null;
                time += Time.unscaledDeltaTime;
                InputTestFixture.Release(Mouse.leftButton, queueEventOnly: true);
                if (i != count - 1 && time < interval)
                    yield return new WaitForSecondsRealtime(interval - time);
            }
        }

        internal static void KeyDown(KeyCode keyCode, float power)
        {
            keyDownPower = power;
            ButtonControl buttonControl = keyCodeToButtonControl(keyCode, power);
            keyDown(keyCode, power, buttonControl);
        }

        internal static void KeyUp(KeyCode keyCode)
        {

            ButtonControl buttonControl = keyCodeToButtonControl(keyCode, keyDownPower);
            keyUp(keyCode, buttonControl);
        }

        internal static IEnumerator KeyPressLifeCycle(KeyCode keyCode, float power, float duration)
        {
            keyDownPower = power;
            ButtonControl buttonControl = keyCodeToButtonControl(keyCode, power);
            yield return null;
            keyDown(keyCode, power, buttonControl);
            yield return new WaitForSeconds(duration);
            keyUp(keyCode, buttonControl, true);
        }

        internal static IEnumerator AccelerationLifeCycle(Vector3 accelerationValue, float duration)
        {
            float currentTime = 0;
            while (currentTime <= duration - Time.unscaledDeltaTime)
            {
                InputTestFixture.Set(Accelerometer.acceleration, accelerationValue * Time.unscaledDeltaTime / duration, queueEventOnly: true);
                yield return null;
                currentTime += Time.unscaledDeltaTime;
                
            }
            InputTestFixture.Set(Accelerometer.acceleration, Vector3.zero);
        }


        internal static IEnumerator MultipointSwipeLifeCycle(UnityEngine.Vector2[] positions, float duration)
        {
            Touchscreen.MakeCurrent();
            float oneTouchDuration = duration / (positions.Length - 1);
            var touchId = BeginTouch(positions[0]);
            yield return null;
            for (int i = 1; i < positions.Length; i++)
            {
                float time = 0;
                Vector2 currentPosition = positions[i - 1];
                var distance = positions[i] - currentPosition;
                while (time < oneTouchDuration)
                {
                    UnityEngine.Vector2 delta;

                    if (time + UnityEngine.Time.unscaledDeltaTime < oneTouchDuration)
                    {
                        delta = distance * UnityEngine.Time.unscaledDeltaTime / oneTouchDuration;
                    }
                    else
                    {
                        delta = positions[i] - currentPosition;
                    }
                    currentPosition += delta;

                    MoveTouch(touchId,currentPosition);
                    yield return null;
                    time += UnityEngine.Time.unscaledDeltaTime;
                }
            }
            endTouchScreenPos = positions[positions.Length - 1];
            EndTouch(touchId);
        }
        internal static int BeginTouch(Vector3 screenPosition)
        {
            var fingerId = 0;
            touches[fingerId] = false;
            InputTestFixture.BeginTouch(fingerId, screenPosition, queueEventOnly: true, screen: Touchscreen);
            return fingerId;
        }

        internal static void MoveTouch(int fingerId, Vector3 screenPosition)
        {
            InputTestFixture.MoveTouch(fingerId, screenPosition, queueEventOnly: true, screen: Touchscreen);
            endTouchScreenPos = screenPosition;
        }

        internal static void EndTouch(int fingerId)
        {
            InputTestFixture.EndTouch(fingerId, endTouchScreenPos, queueEventOnly: true, screen: Touchscreen);
            touches[fingerId] = true;
        }


        #region private interface
        private static ButtonControl keyCodeToButtonControl(KeyCode keyCode, float power = 1)
        {
            foreach (var e in AltUnityKeyMapping.StringToKeyCode)
                if (e.Value == keyCode)
                    return Keyboard[AltUnityKeyMapping.StringToKey[e.Key]];
            foreach (var e in AltUnityKeyMapping.mouseKeyCodeToButtonControl)
                if (e.Key == keyCode)
                    return e.Value;
            AltUnityKeyMapping altUnityKeyMapping = new AltUnityKeyMapping(power);
            foreach (var e in altUnityKeyMapping.joystickKeyCodeToGamepad)
                if (e.Key == keyCode)
                    return e.Value;
            return null;
        }

        private static void setStick(float value, ButtonControl buttonControl)
        {
            if (buttonControl == Gamepad.leftStick.up || buttonControl == Gamepad.leftStick.down)
                InputTestFixture.Set(Gamepad.leftStick.y, value, queueEventOnly: true);
            else if (buttonControl == Gamepad.leftStick.right || buttonControl == Gamepad.leftStick.left)
                InputTestFixture.Set(Gamepad.leftStick.x, value, queueEventOnly: true);
            else if (buttonControl == Gamepad.rightStick.up || buttonControl == Gamepad.rightStick.down)
                InputTestFixture.Set(Gamepad.rightStick.y, value, queueEventOnly: true);
            else if (buttonControl == Gamepad.rightStick.right || buttonControl == Gamepad.rightStick.left)
                InputTestFixture.Set(Gamepad.rightStick.x, value, queueEventOnly: true);
        }

        private static void keyDown(KeyCode keyCode, float power, ButtonControl buttonControl)
        {
            if (keyCode >= KeyCode.JoystickButton16 && keyCode <= KeyCode.JoystickButton19)
                setStick(power, buttonControl);
            else
                InputTestFixture.Press(buttonControl);

        }

        private static void keyUp(KeyCode keyCode, ButtonControl buttonControl, bool queueEventOnly = false)
        {
            if (keyCode >= KeyCode.JoystickButton16 && keyCode <= KeyCode.JoystickButton19)
                setStick(0, buttonControl);
            else
                InputTestFixture.Release(buttonControl, queueEventOnly: queueEventOnly);

        }

        private static int getFreeTouch(bool[] touches)
        {
            for (int i = 1; i < touches.Length; i++)
            {
                if (touches[i]) return i;
            }
            return 0;
        }
        #endregion
    }

}
#if USE_INPUT_SYSTEM_1_3
public class TestExample
{
    [Test]
    public void Test()
    {

    }
}
#endif
#else
namespace Altom.AltUnityTester
{
    public class NewInputSystem
    {

    }
}
#endif
