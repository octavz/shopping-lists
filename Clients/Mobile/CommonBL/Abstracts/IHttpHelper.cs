using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;


namespace CommonBL.Abstracts
{
    public interface IHttpHelper
    {
        string HttpGet<T>(T req,string path) where T : class;

        string HttpPut<T>(T req, string path) where T : class;

        string HttpPatch<T>(T req, string path) where T : class;

        string HttpDelete<T>(T req, string path) where T : class;
    }
}