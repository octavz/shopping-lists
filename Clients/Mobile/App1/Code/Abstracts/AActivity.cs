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
using App1.Extended.Code;

namespace App1.Code.Abstracts
{
    public abstract class AActivity : Activity
    {

        /// <summary>
        /// Shopping list - Application Cotext
        /// </summary>
        public ShApplication ShApplicationContext { get { return (ShApplication)base.ApplicationContext; } }


    }
}