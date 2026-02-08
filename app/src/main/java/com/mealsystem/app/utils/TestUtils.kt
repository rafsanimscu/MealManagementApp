package com.mealsystem.app.utils

import com.mealsystem.app.data.dao.MealSystemDao
import com.mealsystem.app.data.entity.*
import java.util.Date

object TestUtils {

    suspend fun seedTestData(dao: MealSystemDao) {
        // Check if data already exists
        if (dao.login(Constants.DEFAULT_ADMIN_EMAIL, Constants.DEFAULT_ADMIN_PASS) != null) {
            return // Already seeded
        }

        // 1. Create Roles
        dao.insertRole(RoleEntity(roleName = "Admin", permissions = "ALL"))
        dao.insertRole(RoleEntity(roleName = "Member", permissions = "SELF"))

        // 2. Create Admin
        dao.insertUser(
            UserEntity(
                name = "System Admin",
                email = Constants.DEFAULT_ADMIN_EMAIL,
                passwordHash = SecurityUtils.hashPassword(Constants.DEFAULT_ADMIN_PASS),
                roleId = Constants.ROLE_ADMIN,
                mobile = "0000000000",
                room = "Office",
                balance = 0.0,
                isActive = true
            )
        )

        // 3. Create Normal User (For Login/Balance Test)
        dao.insertUser(
            UserEntity(
                name = "John Doe",
                email = "user@test.com",
                passwordHash = SecurityUtils.hashPassword("1234"),
                roleId = Constants.ROLE_MEMBER,
                mobile = "1111111111",
                room = "101",
                balance = 500.0, // Positive Balance
                isActive = true
            )
        )

        // 4. Create Edge Case User (Negative Balance)
        dao.insertUser(
            UserEntity(
                name = "Jane Smith",
                email = "jane@test.com",
                passwordHash = SecurityUtils.hashPassword("1234"),
                roleId = Constants.ROLE_MEMBER,
                mobile = "2222222222",
                room = "102",
                balance = -50.0, // NEGATIVE BALANCE (Edge Case)
                isActive = true
            )
        )

        // 5. Create Settings (Active Month = Current)
        val activeMonth = java.text.SimpleDateFormat("MM-yyyy", java.util.Locale.getDefault()).format(Date())
        dao.insertSettings(
            SettingsEntity(
                costPerMeal = 2.0,
                guestMultiplier = 1.5,
                staffCharge = 0.0,
                activeMonth = activeMonth
            )
        )
    }
}