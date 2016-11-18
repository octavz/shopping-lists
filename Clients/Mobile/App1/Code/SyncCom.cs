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
using ShList.Code.Abstracts;

namespace ShList.Code
{
    public class SyncCom : Java.Lang.Object, IServiceConnection
    {
        AActivity activity;
        SyncBinder binder;


        public SyncCom(AActivity activity)
        {
            this.activity = activity;
        }
        public void OnServiceConnected(ComponentName name, IBinder service)
        {
            var serviceBinder = service as SyncBinder;
            if (serviceBinder != null)
            {                      
              this.binder = (SyncBinder)service;
            }
        }

        public void OnServiceDisconnected(ComponentName name)
        {
            throw new NotImplementedException();
        }

        public SyncBinder Binder
        {
            get
            {
                return binder;
            }
        }

    }
}