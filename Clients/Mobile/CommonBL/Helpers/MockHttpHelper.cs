using CommonBL.Abstracts;
using CommonBL.Data.Request;
using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;



namespace CommonBL.Helpers
{
    public class MockHttpHelper: IHttpHelper
    {        
        Dictionary<Type, string> m_dicTypesGet = new Dictionary<Type, string>()
        {
            {typeof(ReqLoginDTO),@"{email:""aaa@aaa.aa"",userId:3,code:-1}"},
        };
      
        string IHttpHelper.HttpGet<T>(T req,string path) 
        {
            return m_dicTypesGet[typeof(T)];            
        }//HttpGet


        string IHttpHelper.HttpPut<T>(T req, string path)
        {
            return string.Empty;
        }

        string IHttpHelper.HttpPatch<T>(T req, string path)
        {
            return string.Empty;
        }

        string IHttpHelper.HttpDelete<T>(T req, string path)
        {
            return string.Empty;
        }
    }
}