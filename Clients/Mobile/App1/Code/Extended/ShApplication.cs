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

namespace ShList.Extended.Code
{
    [Application]
    public class ShApplication : Application
    {        
        public ShApplication(IntPtr javaReference, JniHandleOwnership transfer) : base(javaReference, transfer)
        {
        }

        /// <summary>
        /// OnCreate
        /// </summary>
        public override void OnCreate()
        {
            base.OnCreate();

        }//OnCreate

    }
}