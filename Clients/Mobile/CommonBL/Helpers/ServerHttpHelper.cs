using CommonBL.Abstracts;
using CommonBL.Utils;
using Newtonsoft.Json;
using System;
using System.Collections.Generic;
using System.IO;
using System.Linq;
using System.Net;
using System.Net.Http;
using System.Net.Http.Headers;
using System.Text;
using System.Threading.Tasks;
using System.Threading;

namespace CommonBL.Helpers
{
    public class ServerHttpHelper : IHttpHelper
    {
        private const string AUTHORIZATION_HEADER = "Bearer";   

        public async Task<string> HttpGet(string path, string authToken)
        {
            return await HttpLoad(path, authToken, HttpMethod.Get);
        }//HttpGet
     

        public async Task<string> HttpDelete(string path, string authToken)
        {
            return await HttpLoad(path, authToken, HttpMethod.Delete);
        }//HttpDelete


        public async Task<string> HttpPut<T>(T req, string path, string authToken) where T : class
        {
            return await HttpSend(req, path, authToken, HttpMethod.Put);
        }//HttpPut

        public async Task<string> HttpPost<T>(T req, string path, string authToken) where T : class
        {
            return await HttpSend(req, path, authToken, HttpMethod.Post);
        }//HttpPost


        /// <summary>
        /// HttpLoad
        /// </summary>
        /// <param name="path"></param>
        /// <param name="authToken"></param>
        /// <returns></returns>
        private async Task<string> HttpLoad(string path, string authToken, HttpMethod verb)
        {
            HttpClient client = SetCustomHttpClient(authToken);

            HttpRequestMessage request = new HttpRequestMessage(verb, path);
            HttpResponseMessage msgRes = null;

            using (msgRes = await client.SendAsync(request))
            {
                string cntRes = await msgRes.Content.ReadAsStringAsync();
                return cntRes;
            }
        }//HttpLoad

        /// <summary>
        /// HttpSend
        /// </summary>
        /// <typeparam name="T"></typeparam>
        /// <param name="req"></param>
        /// <param name="path"></param>
        /// <param name="authToken"></param>
        /// <returns></returns>
        private async Task<string> HttpSend<T>(T req, string path, string authToken, HttpMethod verb) where T : class
        {
            var json = JsonConvert.SerializeObject(req);
            var content = new StringContent(json, Encoding.UTF8, Constants.MEDIA_TYPE);

            HttpClient client = SetCustomHttpClient(authToken);

            HttpRequestMessage request = new HttpRequestMessage(verb, path);
            request.Content = content;

            HttpResponseMessage msgRes = null;

            using (msgRes = await client.SendAsync(request))
            {
                string cntRes = await msgRes.Content.ReadAsStringAsync();
                return cntRes;
            }
        }//HttpSend

        private  HttpClient SetCustomHttpClient(string authToken)
        {
            HttpClient client = new HttpClient();
            client.DefaultRequestHeaders.Authorization = new AuthenticationHeaderValue(AUTHORIZATION_HEADER, authToken ?? string.Empty);
            client.BaseAddress = new Uri(Constants.SERVER);
            client.DefaultRequestHeaders
                  .Accept
                  .Add(new MediaTypeWithQualityHeaderValue(Constants.MEDIA_TYPE));//ACCEPT header
            return client;
        }//SetCustomHttpClient
    }
}
