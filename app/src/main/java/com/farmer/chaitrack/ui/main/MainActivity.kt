package com.farmer.chaitrack.ui.main

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.PopupMenu
import android.view.MenuItem
import android.widget.TextView
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.farmer.chaitrack.R
import com.farmer.chaitrack.databinding.ActivityMainBinding
import com.farmer.chaitrack.ui.auth.ChangePinDialogActivity
import com.farmer.chaitrack.ui.auth.LoginActivity
import com.farmer.chaitrack.ui.delivery.DeliveryActivity
import com.farmer.chaitrack.ui.fertilizer.FertilizerActivity
import com.farmer.chaitrack.ui.finance.FinanceActivity
import com.farmer.chaitrack.ui.notifications.NotificationsActivity
import kotlinx.coroutines.launch
import java.text.NumberFormat
import java.util.Locale

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private val viewModel: MainViewModel by viewModels()
    private var notificationBadge: TextView? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupUI()
        setupBottomNavigation()
        observeState()

        viewModel.loadDashboardData()
    }

    private fun setupUI() {
        binding.ivProfileMenu.setOnClickListener { view ->
            showProfileMenu(view)
        }
    }

    private fun setupBottomNavigation() {
        binding.bottomNavigation.setOnItemSelectedListener { item ->
            when (item.itemId) {
                R.id.nav_deliveries -> {
                    navigateToDeliveries()
                    true
                }
                R.id.nav_fertilizers -> {
                    navigateToFertilizers()
                    true
                }
                R.id.nav_transcripts -> {
                    navigateToFinance()
                    true
                }
                R.id.nav_notifications -> {
                    navigateToNotifications()
                    true
                }
                else -> false
            }
        }

        val notificationMenuItem = binding.bottomNavigation.menu.findItem(R.id.nav_notifications)
        val actionView = notificationMenuItem.actionView
        notificationBadge = actionView?.findViewById(R.id.tvNotificationBadge)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.menu_settings -> {
                val intent = Intent(this, GeneralSettingsActivity::class.java)
                startActivity(intent)
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    private fun showProfileMenu(view: View) {
        val popupMenu = PopupMenu(this, view)
        popupMenu.menuInflater.inflate(R.menu.profile_menu, popupMenu.menu)

        popupMenu.setOnMenuItemClickListener { menuItem ->
            when (menuItem.itemId) {
                R.id.menu_change_pin -> {
                    showChangePinDialog()
                    true
                }
                R.id.menu_logout -> {
                    showLogoutConfirmation()
                    true
                }
                else -> false
            }
        }

        popupMenu.show()
    }

    private fun showChangePinDialog() {
        val intent = Intent(this, ChangePinDialogActivity::class.java)
        startActivityForResult(intent, ChangePinDialogActivity.REQUEST_CODE_CHANGE_PIN)
        overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == ChangePinDialogActivity.REQUEST_CODE_CHANGE_PIN && resultCode == RESULT_OK) {
            Toast.makeText(this, "PIN changed successfully", Toast.LENGTH_SHORT).show()
        }
    }

    private fun showLogoutConfirmation() {
        AlertDialog.Builder(this)
            .setTitle(getString(R.string.logout))
            .setMessage(getString(R.string.confirm_logout))
            .setPositiveButton(getString(R.string.yes)) { _, _ ->
                performLogout()
            }
            .setNegativeButton(getString(R.string.no), null)
            .show()
    }

    private fun performLogout() {
        viewModel.logout()
        Toast.makeText(this, getString(R.string.logout_success), Toast.LENGTH_SHORT).show()

        val intent = Intent(this, LoginActivity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        startActivity(intent)
        finish()
    }

    private fun observeState() {
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.state.collect { state ->
                    updateUI(state)
                }
            }
        }
    }

    private fun updateUI(state: MainState) {
        binding.tvWelcome.text = getString(R.string.welcome_user, state.userName)

        val monthDeliveriesFormatted = formatWeight(state.monthDeliveries)
        findViewById<TextView>(R.id.tvMonthDeliveriesValue)?.text = monthDeliveriesFormatted

        val todayWeightFormatted = formatWeight(state.todayWeight)
        findViewById<TextView>(R.id.tvTodayWeightValue)?.text = todayWeightFormatted

        val earningsFormatted = formatCurrency(state.estimatedEarnings)
        // Make sure this ID exists in your activity_main.xml
        findViewById<TextView>(R.id.tvEstimatedEarningsValue)?.text = earningsFormatted

        val priceFormatted = formatCurrency(state.pricePerKg)
        // Make sure this ID exists in your activity_main.xml
        findViewById<TextView>(R.id.tvPricePerKg)?.text = getString(R.string.price_per_kg, priceFormatted.replace("KSh ", ""))

        updateNotificationBadge(state.unreadNotifications)
    }

    private fun updateNotificationBadge(count: Int) {
        notificationBadge?.let { badge ->
            if (count > 0) {
                badge.visibility = View.VISIBLE
                badge.text = if (count > 99) "99+" else count.toString()
            } else {
                badge.visibility = View.GONE
            }
        }
    }

    private fun formatWeight(weightInKg: Double): String {
        val formatter = NumberFormat.getNumberInstance(Locale.getDefault())
        formatter.maximumFractionDigits = 2
        formatter.minimumFractionDigits = 0
        return getString(R.string.kg_format, formatter.format(weightInKg))
    }

    private fun formatCurrency(amount: Double): String {
        val formatter = NumberFormat.getNumberInstance(Locale.getDefault())
        formatter.maximumFractionDigits = 2
        formatter.minimumFractionDigits = 2
        return getString(R.string.currency_format, formatter.format(amount))
    }

    private fun navigateToDeliveries() {
        val intent = Intent(this, DeliveryActivity::class.java)
        startActivity(intent)
    }

    private fun navigateToFertilizers() {
        val intent = Intent(this, FertilizerActivity::class.java)
        startActivity(intent)
    }

    private fun navigateToFinance() {
        val intent = Intent(this, FinanceActivity::class.java)
        startActivity(intent)
    }

    private fun navigateToNotifications() {
        val intent = Intent(this, NotificationsActivity::class.java)
        startActivity(intent)
        viewModel.markNotificationsAsRead()
    }

    override fun onResume() {
        super.onResume()
        viewModel.loadDashboardData()
    }
}