using CommonBL.Helpers;
using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;


namespace CommonBL.Abstracts
{
    public class HelperFactory
    {
        public const bool PRODUCTION_MODE = true;

        public static IHttpHelper GetHttpHelper()
        {
            if (PRODUCTION_MODE)
                return new ServerHttpHelper();
            else
                return new MockHttpHelper();
        }//GetHttpHelper
    }
}