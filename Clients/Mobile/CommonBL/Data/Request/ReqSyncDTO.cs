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
            lists = new List<FullListDTO>();
        }

        public UserDTO userData { get; set; }

        public ListMetaDTO listsMeta { get; set; }

        public List<FullListDTO> lists { get; set; }
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
            listId = string.Empty;
            boughtItems = new List<string>();
        }

        public string listId { get; set; }
        public List<string> boughtItems { get; set; }
    }

    public class ItemDTO
    {
        public ItemDTO()
        {
            productId = string.Empty;
            description = string.Empty;
        }

        public string productId { get; set; }
        public int quantity { get; set; }
        public string description { get; set; }        
        public int  status { get; set; }
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
            id = string.Empty;
            name = string.Empty;
            description = string.Empty;
            userId = string.Empty;
        }

        public string id { get; set; }
        public string name { get; set; }
        public string description { get; set; }
        public string userId { get; set; }

        public long created { get; set; }
        public int status { get; set; }
    }


    public class UserDTO
    {
        public UserDTO()
        {
            id = string.Empty;
            login = string.Empty;
            password = string.Empty;
            nick = string.Empty;
        }

        public string id { get; set; }
        public string login { get; set; }
        public string password { get; set; }
        public string nick { get; set; }
    }
}
