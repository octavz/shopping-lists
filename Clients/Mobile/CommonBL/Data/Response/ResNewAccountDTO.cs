using CommonBL.Abstracts;
using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace CommonBL.Data.Response
{
    public class ResNewAccountDTO : AResponseDTO
    {
        public string login { get; set; }
        public string password { get; set; }
        public string accessToken { get; set; }
        public string nick { get; set; }
        public string id { get; set; }
    }
}
