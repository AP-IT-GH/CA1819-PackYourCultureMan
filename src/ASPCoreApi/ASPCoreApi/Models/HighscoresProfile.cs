﻿using System;
using System.Collections.Generic;
using System.Linq;
using System.Threading.Tasks;

namespace ASPCoreApi.Models
{
    public class HighscoresProfile
    {
        public int ranking { get; set; }
        public string userName { get; set; }
        public int highestScore { get; set; }
        public int totalScore { get; set; }
        public int totalFailed { get; set; }
        public int totalSucces { get; set; }
        public int totalLost { get; set; }

        
    }
}
