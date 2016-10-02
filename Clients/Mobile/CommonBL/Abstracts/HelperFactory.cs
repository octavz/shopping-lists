using CommonBL.Helpers;
using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;


namespace CommonBL.Abstracts
{
    public class HelperFactory
    {
        public static IHttpHelper GetHttpHelper()
        {         
            return new MockHttpHelper();
        }
    }
}