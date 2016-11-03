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
        public const string URL_LOGIN = "api/login";
        public const string URL_GET_USER = "api/user";
        public const string URL_GET_USER_LISTS = "api/user/{0}/lists";

        public const string URL_CREATE_LIST = "/api/list";
        public const string URL_DELETE_LIST = "/api/list/{0}";

        public const string DEFAULT_LIST_NAME = "_deflistname2016";
    }

    public enum ErrorCodes
    {
        CREATE_ACCOUNT_ALREADY_EXITS = 500,
        UNAUTHORIZED_LOGIN = 401   
    }


}
