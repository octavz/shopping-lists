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

namespace ShList.Code
{
    public class SyncBinder : Binder
    {
        private readonly SyncService _service;

        public SyncBinder(SyncService service)
        {
            _service = service;
        }

        public SyncService GetService()
        {
            return _service;
        }
    }
        
}