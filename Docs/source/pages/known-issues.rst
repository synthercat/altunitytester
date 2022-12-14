============
Known Issues
============

This section lists the known bugs and issues with the AltUnity Tester. If
available, we list a workaround to help troubleshoot the issue.

To report a bug that isn't listed here, see our :doc:`contributing` section
to learn how to best report the issue.


.. contents:: Table of Contents
    :local:
    :depth: 2
    :backlinks: none


Driver
------

Calling ``GetPNGScreenshot`` throws ``StackOverflow`` error (.NET Driver)
~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

**Problem**: For high resolutions calling ``GetPNGScreenshot`` might throw a
``StackOverflow`` error.

**Workaround**: The issue only happens with .NET 6. As a workaround you can use
.NET 5, or if you can't downgrade to .NET 5, try to run your tests with a lower
resolution until this issue is fixed.

**Affects**: AltUnity Tester v1.7.0 with .NET 6


New Input System
----------------

Touch or Mouse actions do not work in tests
~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

**Workaround**: In Analysis -> Input Debugger -> Options make sure the setting
Simulate Touch Input From Mouse or Pen is not checked and Lock Input to
Game View is checked.

**Affects**: AltUnityTester v1.7.1 and Input System with a version below 1.3.0

The PressKey command does not work
~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

**Workaround**: This issue might happen if your code uses the methods
``wasPressedThisFrame`` and ``wasReleasedThisFrame*``. Use ``isPressed``
instead.

**Affects**: AltUnityTester v1.7.1

Player Input is not working when connected to AltUnity Tester/Inspector
~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

**Problem**: Player Input is not working in the instrumented build when
connected to the tests or to AltUnity Inspector.

**Affects**: All input actions created with the New Input System for game
objects. The New Input System actions for UI objects are not affected, as well
as the Old Input System actions.


BlueStacks
----------

Instrumented game with AltUnity Tester stops working in BlueStacks
~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

**Workaround**: The issue seems to happen because of the communication protocol
used inside the AltUnity Tester. There are other alternatives that work with
instrumented builds. For Android you can use the Android Emulator inside the
Android Studio. For iOS you can use a simulator inside Xcode.

**Affects**: AltUnity Tester v1.7.0 and v1.7.1
