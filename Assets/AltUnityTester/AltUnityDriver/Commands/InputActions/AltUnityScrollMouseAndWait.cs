namespace Altom.AltUnityDriver.Commands
{
    public class AltUnityScrollMouseAndWait : AltBaseCommand
    {
        float speed;
        float duration;
        public AltUnityScrollMouseAndWait(SocketSettings socketSettings, float speed, float duration) : base(socketSettings)
        {
            this.speed = speed;
            this.duration = duration;
        }
        public void Execute()
        {
            new AltUnityScrollMouse(SocketSettings, speed, duration).Execute();
            System.Threading.Thread.Sleep((int)duration * 1000);
            string data;
            do
            {
                SendCommand("actionFinished");
                data = Recvall();
            } while (data == "No");
            if (data.Equals("Yes"))
                return;
            HandleErrors(data);
        }
    }
}