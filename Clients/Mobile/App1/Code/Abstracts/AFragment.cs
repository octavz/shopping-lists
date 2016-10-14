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
using ShList.Extended.Code;

namespace ShList.Code.Abstracts
{
    public abstract class AFragment: Fragment
    {

        public ShApplication ShApplicationContext { get { return ((AActivity)this.Activity).ShApplicationContext; } }
    }
}