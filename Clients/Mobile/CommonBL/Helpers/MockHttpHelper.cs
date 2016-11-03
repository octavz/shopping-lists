using CommonBL.Abstracts;
using CommonBL.Data.Request;
using CommonBL.Utils;
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

        async Task<string> IHttpHelper.HttpGet(string path, string authToken) 
        {
            return await Task.Run(() => path == Constants.URL_GET_USER ? "": string.Empty);
        }//HttpGet


        async Task<string> IHttpHelper.HttpPut<T>(T req, string path, string authToken)
        {
            return await Task.Run(() => m_dicTypesPut[typeof(T)]);
        }

        async Task<string> IHttpHelper.HttpPatch<T>(T req, string path, string authToken)
        {
            return await Task.Run(() => m_dicTypesPatch[typeof(T)]);
        }

        async Task<string> IHttpHelper.HttpDelete(string path, string authToken)
        {
            return await Task.Run(() => path == Constants.URL_DELETE_LIST ? "" : string.Empty);
        }
    }
}