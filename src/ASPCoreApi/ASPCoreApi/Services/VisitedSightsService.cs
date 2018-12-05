using ASP.Helpers;
using ASPCoreApi.Models;
using Microsoft.AspNetCore.Mvc;
using System;
using System.Collections.Generic;
using System.Linq;
using System.Threading.Tasks;

namespace ASPCoreApi.Services
{
    public interface IVisitedSightsService
    {
        List<VisitedSights> getByUserId(int id);
        List<VisitedSights> addVisitedSights(Users userParam);
        List<VisitedSights> UpdateVisitedSights(Users userParam);
        void Delete(Users userParam);
    }
    public class VisitedSightsService : IVisitedSightsService
    {
        DatabaseContext _context;

        public VisitedSightsService(DatabaseContext context)
        {
            _context = context;
        }


        public List<VisitedSights> getByUserId(int id)
        {
            IQueryable<VisitedSights> visitedSights = _context.visitedSights;
            visitedSights = visitedSights.Where(d => d.userId == id);

            var resultList = visitedSights.ToList();

            return resultList;
        }
        //overbodig aangezien de lijst wordt aangemaakt bij de registratie
        public List<VisitedSights> addVisitedSights(Users userParam)
        {
            var user = _context.users.Find(userParam.Id);
            var visitedList = userParam.visitedSights.ToList();


            if (user == null)
                throw new AppException("User not found");
            foreach(var visited in visitedList)
            {
                _context.visitedSights.Add(visited);
            }
            _context.SaveChanges();
            return visitedList;
        }
        public List<VisitedSights> UpdateVisitedSights(Users userParam)
        {
            
            var visitedSightsListDb = getByUserId(userParam.Id);

            
            var visitedSightsListIn = userParam.visitedSights.ToList();
            if (visitedSightsListDb == null)
                throw new AppException("visited list not found");
            for (int i = 0; i < visitedSightsListDb.Count(); i++)
            {
                visitedSightsListDb[i].isChecked = visitedSightsListIn[i].isChecked;
                var visitedSightToUpdate = visitedSightsListDb[i];
                _context.visitedSights.Update(visitedSightToUpdate);
            }
            _context.SaveChanges();

            return visitedSightsListDb;
        }
        public void Delete(Users userParam)
        {
            var sights = getByUserId(userParam.Id);

            if (sights != null)
            {
                foreach (var sight in sights)
                {
                    _context.visitedSights.Remove(sight);
                }
                              
                _context.SaveChanges();
            }
        }
        
    }
}
