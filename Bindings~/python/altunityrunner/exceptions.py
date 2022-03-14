"""This module contains the set of AltUnity Tester's exceptions."""


class AltUnityException(Exception):
    """Base exception class for AltUnity."""


class ConnectionError(AltUnityException):
    """Raised when the client can not connect to the server."""


class ConnectionTimeoutError(ConnectionError):
    """Raised when the client connection timesout."""


class AltUnityInvalidServerResponse(AltUnityException):
    """Raised when the server responds with an invalid respose."""

    def __init__(self, expected, received):
        super().__init__("Expected to get response {}; got {}".format(expected, received))


class InvalidParameterTypeException(TypeError, AltUnityException):
    """Raised when an function or method receives an parameter that has the inappropriate type."""

    def __init__(self, parameter_name, expected_types, received_type):
        expected_types = [expected_type.__name__ for expected_type in expected_types]
        expected_types = ", ".join(expected_types)

        super().__init__(
            "TypeError: {} must be {}; not {}.".format(parameter_name, expected_types, received_type.__name__)
        )


class InvalidParameterValueException(ValueError, AltUnityException):
    """Raised when an function or method receives an parameter that has the right type but an inappropriate value."""


class NotFoundException(AltUnityException):
    """Raised when a object, camera, component, property, method or assembly is not found."""


class SceneNotFoundException(NotFoundException):
    """Raised when a scene is not found."""


class ObjectNotFoundException(NotFoundException):
    """Raised when a object is not found."""


class CameraNotFoundException(NotFoundException):
    """Raised when a camera is not found."""


class PropertyNotFoundException(NotFoundException):
    """Raised when a property is not found."""


class MethodNotFoundException(NotFoundException):
    """Raised when a method is not found."""


class MethodWithGivenParametersNotFoundException(NotFoundException):
    """Raised when a method with the given parameters is not found."""


class ComponentNotFoundException(NotFoundException):
    """Raised when a component is not found."""


class AssemblyNotFoundException(NotFoundException):
    """Raised when an assembly is not found."""


class CallMethodException(AltUnityException):
    pass


class CouldNotPerformOperationException(AltUnityException):
    pass


class CouldNotParseJsonStringException(AltUnityException):
    pass


class NullReferenceException(AltUnityException):
    """Raised when there is an attempt to dereference a null object reference."""


class FailedToParseArgumentsException(AltUnityException):
    pass


class WaitTimeOutException(AltUnityException):
    """Raised when a command timed out."""


class CommandResponseTimeoutException(AltUnityException):
    pass


class PropertyCannotBeSetException(AltUnityException):
    pass


class FormatException(AltUnityException):
    pass


class AltUnityInvalidPathException(AltUnityException):
    pass


class AltUnityInputModuleException(AltUnityException):
    pass


class UnknownErrorException(AltUnityException):
    """Raised when an unexpected error occurred."""


class AltUnityReceiveMessageIdException(AltUnityException):
    """Raised when an response has different id than the command"""
