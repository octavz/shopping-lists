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
using ShList.Code.Data.Request;

namespace ShList.Code.Helpers
{
    public class MockHttpHelper: IHttpHelper
    {        
        Dictionary<Type, string> m_dicTypes = new Dictionary<Type, string>()
        {
            {typeof(ReqLoginDTO),@"{email:""aaa@aaa.aa"",userId:3}"},
        };
      
        string IHttpHelper.HttpGet<T>(T req,string path) 
        {
            return m_dicTypes[typeof(T)];            
        }//HttpGet

    }
}