using System;
using System.Collections.Generic;
using System.Linq;
using System.Threading.Tasks;

namespace ASPCoreApi.Models
{
    public class GameStats
    {
        public int Id { get; set; }
        public int lifePoints { get; set; }
        public int rifle { get; set; }
        public int pushBackGun { get; set; }
        public int freezeGun { get; set; }
    }
}
