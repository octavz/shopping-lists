using CommonBL.Abstracts;
using CommonBL.Data.Request;
using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace CommonBL.Helpers
{
    public class MockHttpHelper: IHttpHelper
    {        
        Dictionary<Type, string> m_dicTypesGet = new Dictionary<Type, string>()
        {
            {typeof(ReqLoginDTO),@"{email:""aaa@aaa.aa"",userId:3,code:-1}"},
        };

        Dictionary<Type, string> m_dicTypesPut = new Dictionary<Type, string>()
        { };

        Dictionary<Type, string> m_dicTypesPatch = new Dictionary<Type, string>()
        { };

        Dictionary<Type, string> m_dicTypesDelete = new Dictionary<Type, string>()
        { };

        async Task<string> IHttpHelper.HttpGet<T>(T req,string path) 
        {
            return await Task.Run(() => m_dicTypesGet[typeof(T)]);
        }//HttpGet


        async Task<string> IHttpHelper.HttpPut<T>(T req, string path)
        {
            return await Task.Run(() => m_dicTypesPut[typeof(T)]);
        }

        async Task<string> IHttpHelper.HttpPatch<T>(T req, string path)
        {
            return await Task.Run(() => m_dicTypesPatch[typeof(T)]);
        }

        async Task<string> IHttpHelper.HttpDelete<T>(T req, string path)
        {
            return await Task.Run(() => m_dicTypesDelete[typeof(T)]);
        }
    }
}