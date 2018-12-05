using ASPCoreApi.Models;
using Newtonsoft.Json.Linq;
using System;
using System.Collections.Generic;
using System.Linq;
using System.Threading.Tasks;

namespace ASPCoreApi.Helpers
{
    public class AuthenticationObject
    {        
        public string token { get; set; }
        public JObject user { get; set; }
    }
}
