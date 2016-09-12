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
using ShList.Code.Helpers;

namespace ShList.Code.Abstracts
{
    public class HelperFactory
    {
        public static IHttpHelper GetHttpHelper()
        {         
            return new MockHttpHelper();
        }
    }
}