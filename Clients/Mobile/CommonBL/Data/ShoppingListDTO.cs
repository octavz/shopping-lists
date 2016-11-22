using CommonBL.Data.Request;
using CommonBL.Data.Response;
using CommonBL.Utils;
using Newtonsoft.Json;
using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;


namespace CommonBL.Data
{
    public class ShoppingListDTO
    {

        public ShoppingListDTO()
        {
            ListName = string.Empty;
            IsDirty = false;
        }

        public ShoppingListDTO(ResListDTO aList)
        {            
        }

        public string Id { get; set; }//DB id

        public string InternalId { get; set; }//Application level only

        public string ListName { get; set; }

        public string ListDescription { get; set; }

        public DateTime ListDate { get; set; }

        public bool IsDirty { get; set; }//used for sync
      

    }
}