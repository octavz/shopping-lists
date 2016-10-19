using System;
using System.Collections.Generic;
using System.Linq;
using System.Net.Mail;
using System.Text;
using System.Threading.Tasks;

namespace CommonBL.Utils
{
    public static class Tools
    {
        public  static bool IsEmailValid(string emailaddress)
        {
            try
            {
                MailAddress m = new MailAddress(emailaddress);
                return true;
            }//try
            catch (FormatException)
            {
                return false;
            }
        }//IsEmailValid

    }
}
