using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;

namespace CommonBL.Data.Response
{
    public class ResLoginDTO
    {
        public string Email { get; set; }
        public int UserId { get; set; }
        public int  Code { get; set; } //if is successfull is 0 else error
    }
}