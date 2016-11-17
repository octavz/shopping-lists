using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;

using Android.App;
using Android.Content;
using Android.OS;
using Android.Runtime;
using Android.Views;
using Android.Widget;
using Android.Util;
using System.Threading;
using CommonBL.Managers;
using CommonBL.Data;
using ShList.Code.Common;
using Android.Support.V4.Content;

namespace ShList.Code
{
    class TimerState
    {
        public int counter = 0;
        public Timer tmr;        
    }

    class SyncHandler : Handler
    {
        public override void HandleMessage(Message msg)
        {
        }
    }

    [Service]
    public class SyncService : Service
    {
        TimerState stateClient = new TimerState();
        TimerState stateServer = new TimerState();
        Messenger msg;
        Context cnx;

        public SyncService()
        {
            msg = new Messenger(new SyncHandler());
        }

        public override IBinder OnBind(Intent intent)
        {
            return msg.Binder;
        }

        public override StartCommandResult OnStartCommand(Android.Content.Intent intent, StartCommandFlags flags, int startId)
        {
            Log.Debug("SyncService", "SyncService started");

            TimerCallback timerClientDelegate = new TimerCallback(CheckClientChanges);
            // Create a timer that waits one second, then invokes every second.
            Timer timerClient = new Timer(timerClientDelegate, stateClient, 1000, 10000);
            stateClient.tmr = timerClient;


            TimerCallback timerServerDelegate = new TimerCallback(CheckServerChanges);
            // Create a timer that waits one second, then invokes every second.
            Timer timerServer = new Timer(timerServerDelegate, stateClient, 1000, 15000);
            stateServer.tmr = timerServer;

            cnx = this;

            return StartCommandResult.NotSticky;
        }//OnStartCommand


        void CheckClientChanges(Object state)
        {
            Log.Debug("SyncService", "Start sync - Client");
            TimerState s = (TimerState)state;

            List<ShoppingListDTO> lsts = ListsManager.Instance.Lists.Where(x => x.IsDirty).ToList();
            if (lsts.Count == 0)
                return;

            lsts.ForEach(x => x.IsDirty = false);
            string sJson = ListsManager.Instance.GetSerializedData();
            FilesManager.WriteShListsState(sJson);
            Log.Debug("SyncService", "Start sync - Save in file the changes");
        }//CheckClientChanges

        void CheckServerChanges(Object state)
        {
            Log.Debug("SyncService", "Start sync - Server");
            TimerState s = (TimerState)state;

            Intent intent = new Intent();
            intent.SetAction(Intent.ActionSend);
            //intent.PutExtra("UpdateUi", message);
            intent.PutExtra("UpdateUi", true);
            LocalBroadcastManager.GetInstance(cnx).SendBroadcast(intent);
        }//CheckServerChanges
    }
}