using System;
using System.Collections.Generic;
using System.Linq;
using System.Threading.Tasks;

namespace ASPCoreApi.Models
{
    public class Dot
    {
        public int id { get; set; }
        public string Latitude { get; set; }
        public string Longitude { get; set; }
        public Boolean Taken { get; set; }
    }
}
