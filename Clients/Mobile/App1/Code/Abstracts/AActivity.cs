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
using Android.Support.V7.App;
using ShList.Extended.Code;

namespace ShList.Code.Abstracts
{
    public abstract class AActivity : AppCompatActivity
    {

        /// <summary>
        /// Shopping list - Application Cotext
        /// </summary>
        public ShApplication ShApplicationContext { get { return (ShApplication)base.ApplicationContext; } }
        
        
    }
}