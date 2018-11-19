using Microsoft.EntityFrameworkCore.Migrations;

namespace ASPCoreApi.Migrations
{
    public partial class stats2 : Migration
    {
        protected override void Up(MigrationBuilder migrationBuilder)
        {
            migrationBuilder.DropForeignKey(
                name: "FK_users_stats_StatsStatisticsId",
                table: "users");

            migrationBuilder.RenameColumn(
                name: "StatsStatisticsId",
                table: "users",
                newName: "StatsId");

            migrationBuilder.RenameIndex(
                name: "IX_users_StatsStatisticsId",
                table: "users",
                newName: "IX_users_StatsId");

            migrationBuilder.RenameColumn(
                name: "StatisticsId",
                table: "stats",
                newName: "Id");

            migrationBuilder.AddForeignKey(
                name: "FK_users_stats_StatsId",
                table: "users",
                column: "StatsId",
                principalTable: "stats",
                principalColumn: "Id",
                onDelete: ReferentialAction.Restrict);
        }

        protected override void Down(MigrationBuilder migrationBuilder)
        {
            migrationBuilder.DropForeignKey(
                name: "FK_users_stats_StatsId",
                table: "users");

            migrationBuilder.RenameColumn(
                name: "StatsId",
                table: "users",
                newName: "StatsStatisticsId");

            migrationBuilder.RenameIndex(
                name: "IX_users_StatsId",
                table: "users",
                newName: "IX_users_StatsStatisticsId");

            migrationBuilder.RenameColumn(
                name: "Id",
                table: "stats",
                newName: "StatisticsId");

            migrationBuilder.AddForeignKey(
                name: "FK_users_stats_StatsStatisticsId",
                table: "users",
                column: "StatsStatisticsId",
                principalTable: "stats",
                principalColumn: "StatisticsId",
                onDelete: ReferentialAction.Restrict);
        }
    }
}
