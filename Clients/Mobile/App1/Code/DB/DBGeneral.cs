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
using System.IO;

namespace ShList.Code.DB
{
    public class DBGeneral: DBBasic
    {
        /// <summary>
        /// DbExists
        /// </summary>
        /// <returns></returns>
        public bool DbExists()
        {
            return File.Exists(GetDBPath());
        }//DbExists
        
    }
}