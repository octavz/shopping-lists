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
using ShList.Extended.Code;
using CommonBL.Abstracts;
using CommonBL.Data.Request;
using CommonBL.Utils;
using System.Threading.Tasks;
using Newtonsoft.Json;
using CommonBL.Data.Response;

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
        object mLockerClient = new object();
        object mLockerServer = new object();

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
            Timer timerServer = new Timer(timerServerDelegate, stateClient, 0, 15000);
            stateServer.tmr = timerServer;

            cnx = this;

            return StartCommandResult.NotSticky;
        }//OnStartCommand


        void CheckClientChanges(Object state)
        {
            lock (mLockerClient)
            {
                string sLogTime = "SyncService - Client";
                Log.Debug(sLogTime, "Start sync - Client");
                TimerState s = (TimerState)state;

                List<ShoppingListDTO> lsts = ListsManager.Instance.Lists.Where(x => x.IsDirty).ToList();
                if (lsts.Count == 0)
                    return;

                SyncRequestResponseStorage(sLogTime, UpdatedUI, true);
            }
        }//CheckClientChanges

        void CheckServerChanges(Object state)
        {
            lock (mLockerServer)
            {
                string sLogTime = "SyncService - Server";
                Log.Debug(sLogTime, "Start sync - Server");
                TimerState s = (TimerState)state;

                List<ShoppingListDTO> lsts = ListsManager.Instance.Lists.Where(x => x.IsDirty).ToList();
                if (lsts.Count != 0)
                    return;

                SyncRequestResponseStorage(sLogTime, UpdatedUI, false);
            }
        }//CheckServerChanges

        /// <summary>
        /// SyncRequestResponseStorage
        /// </summary>
        /// <param name="logTimerType"></param>
        /// <param name="syncUi"></param>
        private void SyncRequestResponseStorage(string logTimerType, Action SyncUi, bool SaveWithSameHash)
        {            
            if (string.IsNullOrEmpty(ShAppContext.UserId))
                return;

            ReqSyncDTO SyncDTO = ListsManager.Instance.GenerateRequestDTOForSync(ShAppContext.UserId, ShAppContext.UserLogin);
            Task<string> resSync = HelperFactory.GetHttpHelper().HttpPut<ReqSyncDTO>(SyncDTO, Constants.URL_SYNC, ShAppContext.UserToken);
            string sResJson = resSync.Result;
            Log.Debug(logTimerType, "Send request server DTO");

            ResSyncDTO ResSync = null;
            try
            {
                ResSync = JsonConvert.DeserializeObject<ResSyncDTO>(sResJson);
                Log.Debug(logTimerType, "OK response from server");
            }
            catch (Exception)
            {
                ResSync = new ResSyncDTO();
                Log.Debug(logTimerType, "Exception deserialize response from server");
            }

            string newhash = Guid.NewGuid().ToString();
            if (ResSync.ErrorCode == 0)
            {
                newhash = Tools.GetMd5Hash(sResJson);
                if (newhash != ListsManager.Instance.CurrentJsonHash && SyncUi != null)
                {
                    ListsManager.Instance.ImportSyncData(newhash, ResSync);
                    SyncUi();
                }//endif
                Log.Debug(logTimerType, "Sync OK");
            }
            else
                Log.Debug(logTimerType, "Error response from server - NOT sync");

            if ((SaveWithSameHash && newhash == ListsManager.Instance.CurrentJsonHash) || (newhash != ListsManager.Instance.CurrentJsonHash))
            {
                string sJson = ListsManager.Instance.GetSerializedDataForLocalStorage();
                FilesManager.WriteShListsState(sJson);
                Log.Debug(logTimerType, "Save in file the changes");
            }//endif
        }//SyncRequestResponseStorage



        private void UpdatedUI()
        {
            Intent intent = new Intent();
            intent.SetAction(Intent.ActionSend);
            //intent.PutExtra("UpdateUi", message);
            intent.PutExtra("UpdateUi", true);
            LocalBroadcastManager.GetInstance(cnx).SendBroadcast(intent);
        }//UpdatedUI

        private ShApplication ShAppContext { get { return (ShApplication)Application.Context; } }

    }
}