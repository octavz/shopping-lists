using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace CommonBL.Abstracts
{
    public interface IHttpHelper
    {
        Task<string> HttpGet<T>(T req,string path) where T : class;

        Task<string> HttpPut<T>(T req, string path) where T : class;

        Task<string> HttpPatch<T>(T req, string path) where T : class;

        Task<string> HttpDelete<T>(T req, string path) where T : class;
    }
}