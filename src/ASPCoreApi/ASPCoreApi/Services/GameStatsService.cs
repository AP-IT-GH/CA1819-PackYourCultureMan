using ASP.Helpers;
using ASPCoreApi.Models;
using System;
using System.Collections.Generic;
using System.Linq;
using System.Threading.Tasks;

namespace ASPCoreApi.Services
{
    public interface IGameStatsService
    {

        GameStats getByUserId(int id);
        Users UpdateGameStats(Users userParam);
        void Delete(Users userParam);

    }
    public class GameStatsService : IGameStatsService
    {
        private DatabaseContext _context;

        public GameStatsService(DatabaseContext context)
        {
            _context = context;
        }

        public GameStats getByUserId(int id)
        {
            IQueryable<GameStats> gamestats = _context.gameStats;
            var result = gamestats.Single(d => d.userId == id);

            return result;
        }

        public Users UpdateGameStats(Users userParam)
        {
            var user = _context.users.Find(userParam.Id);
            var gameStats = getByUserId(userParam.Id);
            if (user == null)
                throw new AppException("User not found");
            if (gameStats == null)
                throw new AppException("gamestats not found");

            //update stats
            if (userParam.gameStats.lifePoints != 0) gameStats.lifePoints = userParam.gameStats.lifePoints;
            if (userParam.gameStats.rifle != 0) gameStats.rifle = userParam.gameStats.rifle;
            if (userParam.gameStats.freezeGun != 0) gameStats.freezeGun = userParam.gameStats.freezeGun;
            if (userParam.gameStats.pushBackGun != 0) gameStats.pushBackGun = userParam.gameStats.pushBackGun;
            if (userParam.gameStats.coins != 0) gameStats.coins = userParam.gameStats.coins;

            if (userParam.gameStats.lifePoints == -1) gameStats.lifePoints = 0;
            if (userParam.gameStats.rifle == -1) gameStats.rifle = 0;
            if (userParam.gameStats.freezeGun == -1) gameStats.freezeGun = 0;
            if (userParam.gameStats.pushBackGun == -1) gameStats.pushBackGun = 0;
            if (userParam.gameStats.coins == -1) gameStats.coins = 0;


            _context.gameStats.Update(gameStats);
            _context.users.Update(user);
            _context.SaveChanges();
            user.gameStats = gameStats;
            return user;
        }
        public void Delete(Users userParam)
        {
            var gameStats = getByUserId(userParam.Id);

            if (gameStats != null)
            {
                _context.gameStats.Remove(gameStats);
                _context.SaveChanges();
            }
        }
    }
}
