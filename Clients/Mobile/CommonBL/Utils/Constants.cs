using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace CommonBL.Utils
{
    public class Constants
    {
        public const string SERVER = "http://shoplist.ml:9000/";
        public const string URL_CREATE_ACCOUNT = "api/register";
        
    }

    public enum ErrorCodes
    {
        CREATE_ACCOUNT_ALREADY_EXITS = 500
    }
}
