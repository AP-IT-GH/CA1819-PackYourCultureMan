using System;
using System.Collections.Generic;
using System.Linq;
using System.Threading.Tasks;

namespace ASPCoreApi.Models
{
    public class Sight
    {
        public int id { get; set; }
        public string Name { get; set; }
        public string Description { get; set; }
        public float Longitude { get; set; }
        public float Latitude { get; set; }
        public byte[] sightImage { get; set; } 
    }
}
