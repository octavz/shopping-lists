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

namespace App1.Code.Abstracts
{
    public interface IHttpHelper
    {
        string HttpGet<T>(T req,string path) where T : class;
    }
}