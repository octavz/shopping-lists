using Newtonsoft.Json;
using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace CommonBL.Data.Request
{
    public class ReqSyncDTO
    {
        public ReqSyncDTO()
        {
            userData = new UserDTO();
            listsMeta = new ListMetaDTO();
        }

        public UserDTO userData { get; set; }

        public ListMetaDTO listsMeta { get; set; }
        
    }//ReqSyncDTO

    public class FullListDTO
    {
        public FullListDTO()
        {
            items = new List<ItemDTO>();
            meta = new ItemsMetaDTO();
        }

        public List<ItemDTO> items { get; set; }

        public ItemsMetaDTO meta { get; set; }
    }

    public class ItemsMetaDTO
    {
        public ItemsMetaDTO()
        {            
            boughtItems = new List<string>();
        }

        public string listId { get; set; }
        public List<string> boughtItems { get; set; }
    }

    public class ItemDTO
    {
        public ItemDTO()
        {
          
        }

        public string productId { get; set; }
        public int quantity { get; set; }
        public string description { get; set; }        
        public int  status { get; set; }

        public string clientTag { get; set; }
    }

    public class ListMetaDTO
    {
        public ListMetaDTO()
        {
            items = new List<ShListDTO>();
        }

        public List<ShListDTO> items { get; set; }
        public int total { get; set; }
    }

    public class ShListDTO
    {
        public ShListDTO()
        {           
        }

        public string id { get; set; }
        public string name { get; set; }
        public string description { get; set; }
        public string userId { get; set; }
        public long created { get; set; }
        public int status { get; set; }
        public string clientTag { get; set; }
        public List<ItemDTO> items{ get; set; }
}


    public class UserDTO
    {
        public UserDTO()
        {
        }

        public string id { get; set; }
        public string login { get; set; }
        public string password { get; set; }
        public string nick { get; set; }
    }
}
