package com.farmer.chaitrack.ui.finance.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.farmer.chaitrack.R
import com.farmer.chaitrack.domain.model.FinanceRecord

/**
 * Adapter for displaying finance/salary transcript records in RecyclerView
 */
class FinanceAdapter(
    private val onItemClick: (FinanceRecord) -> Unit,
    private val onGeneratePdf: (FinanceRecord) -> Unit,
    private val onShareWithBank: (FinanceRecord) -> Unit
) : ListAdapter<FinanceRecord, FinanceAdapter.FinanceViewHolder>(FinanceDiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FinanceViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_finance_record, parent, false)
        return FinanceViewHolder(view, onItemClick, onGeneratePdf, onShareWithBank)
    }

    override fun onBindViewHolder(holder: FinanceViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    /**
     * ViewHolder for finance record items
     */
    class FinanceViewHolder(
        itemView: View,
        private val onItemClick: (FinanceRecord) -> Unit,
        private val onGeneratePdf: (FinanceRecord) -> Unit,
        private val onShareWithBank: (FinanceRecord) -> Unit
    ) : RecyclerView.ViewHolder(itemView) {

        private val tvMonth: TextView = itemView.findViewById(R.id.tvMonth)
        private val tvYear: TextView = itemView.findViewById(R.id.tvYear)
        private val tvTotalWeight: TextView = itemView.findViewById(R.id.tvTotalWeight)
        private val tvPricePerKg: TextView = itemView.findViewById(R.id.tvPricePerKg)
        private val tvTotalEarnings: TextView = itemView.findViewById(R.id.tvTotalEarnings)
        private val tvNetEarnings: TextView = itemView.findViewById(R.id.tvNetEarnings)
        private val tvDeductions: TextView = itemView.findViewById(R.id.tvDeductions)
        private val btnGeneratePdf: View = itemView.findViewById(R.id.btnGeneratePdf)
        private val btnShareWithBank: View = itemView.findViewById(R.id.btnShareWithBank)
        private val viewPdfIndicator: View = itemView.findViewById(R.id.viewPdfIndicator)

        fun bind(record: FinanceRecord) {
            // Set month and year
            tvMonth.text = record.getMonthDisplayText()
            tvYear.text = record.year.toString()

            // Set weight and pricing
            tvTotalWeight.text = record.getFormattedTotalWeight()
            tvPricePerKg.text = record.getFormattedPricePerKg()

            // Set earnings
            tvTotalEarnings.text = record.getFormattedTotalEarnings()
            tvNetEarnings.text = record.getFormattedNetEarnings()

            // Set deductions if any
            if (record.deductions > 0) {
                tvDeductions.visibility = View.VISIBLE
                tvDeductions.text = "Deductions: ${record.getFormattedDeductions()}"
            } else {
                tvDeductions.visibility = View.GONE
            }

            // Show PDF indicator if PDF exists
            viewPdfIndicator.visibility = if (record.hasPdf()) View.VISIBLE else View.GONE

            // Update button states
            if (record.hasPdf()) {
                btnGeneratePdf.isEnabled = false
                (btnGeneratePdf as? TextView)?.text = "PDF Generated"
                btnShareWithBank.isEnabled = true
            } else {
                btnGeneratePdf.isEnabled = true
                (btnGeneratePdf as? TextView)?.text = "Generate PDF"
                btnShareWithBank.isEnabled = false
            }

            // Click listeners
            itemView.setOnClickListener { onItemClick(record) }
            btnGeneratePdf.setOnClickListener {
                if (record.hasPdf()) {
                    onItemClick(record) // View existing PDF
                } else {
                    onGeneratePdf(record) // Generate new PDF
                }
            }
            btnShareWithBank.setOnClickListener { onShareWithBank(record) }
        }
    }

    /**
     * DiffUtil callback for efficient list updates
     */
    private class FinanceDiffCallback : DiffUtil.ItemCallback<FinanceRecord>() {
        override fun areItemsTheSame(oldItem: FinanceRecord, newItem: FinanceRecord): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: FinanceRecord, newItem: FinanceRecord): Boolean {
            return oldItem == newItem
        }
    }
}

/**
 * Adapter for displaying finance summary on Main Activity
 */
class FinanceSummaryAdapter(
    private val onViewDetails: () -> Unit
) : RecyclerView.Adapter<FinanceSummaryAdapter.SummaryViewHolder>() {

    private var monthlyDeliveries: Double = 0.0
    private var todayWeight: Double = 0.0
    private var estimatedEarnings: Double = 0.0
    private var currentPricePerKg: Double = 0.0

    fun updateData(
        monthlyDeliveries: Double,
        todayWeight: Double,
        estimatedEarnings: Double,
        currentPricePerKg: Double
    ) {
        this.monthlyDeliveries = monthlyDeliveries
        this.todayWeight = todayWeight
        this.estimatedEarnings = estimatedEarnings
        this.currentPricePerKg = currentPricePerKg
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SummaryViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_finance_summary, parent, false)
        return SummaryViewHolder(view, onViewDetails)
    }

    override fun onBindViewHolder(holder: SummaryViewHolder, position: Int) {
        holder.bind(monthlyDeliveries, todayWeight, estimatedEarnings, currentPricePerKg)
    }

    override fun getItemCount(): Int = 1

    /**
     * ViewHolder for finance summary
     */
    class SummaryViewHolder(
        itemView: View,
        private val onViewDetails: () -> Unit
    ) : RecyclerView.ViewHolder(itemView) {

        private val tvMonthlyDeliveries: TextView = itemView.findViewById(R.id.tvMonthlyDeliveries)
        private val tvTodayWeight: TextView = itemView.findViewById(R.id.tvTodayWeight)
        private val tvEstimatedEarnings: TextView = itemView.findViewById(R.id.tvEstimatedEarnings)
        private val tvCurrentPrice: TextView = itemView.findViewById(R.id.tvCurrentPrice)
        private val btnViewDetails: View = itemView.findViewById(R.id.btnViewDetails)

        fun bind(
            monthlyDeliveries: Double,
            todayWeight: Double,
            estimatedEarnings: Double,
            currentPricePerKg: Double
        ) {
            tvMonthlyDeliveries.text = "${"%.1f".format(monthlyDeliveries)} kg"
            tvTodayWeight.text = "${"%.1f".format(todayWeight)} kg"
            tvEstimatedEarnings.text = "KES ${"%,.2f".format(estimatedEarnings)}"
            tvCurrentPrice.text = "KES ${"%.2f".format(currentPricePerKg)}/kg"

            btnViewDetails.setOnClickListener { onViewDetails() }
        }
    }
}

/**
 * Adapter for month/year selector dropdowns
 */
class MonthYearAdapter : RecyclerView.Adapter<MonthYearAdapter.MonthYearViewHolder>() {

    private val items = mutableListOf<String>()
    private var selectedPosition = 0
    private var onItemSelected: ((Int, String) -> Unit)? = null

    fun setItems(items: List<String>) {
        this.items.clear()
        this.items.addAll(items)
        notifyDataSetChanged()
    }

    fun setSelectedPosition(position: Int) {
        val oldPosition = selectedPosition
        selectedPosition = position
        notifyItemChanged(oldPosition)
        notifyItemChanged(selectedPosition)
    }

    fun setOnItemSelectedListener(listener: (Int, String) -> Unit) {
        onItemSelected = listener
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MonthYearViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_month_year, parent, false)
        return MonthYearViewHolder(view)
    }

    override fun onBindViewHolder(holder: MonthYearViewHolder, position: Int) {
        holder.bind(items[position], position == selectedPosition) {
            setSelectedPosition(position)
            onItemSelected?.invoke(position, items[position])
        }
    }

    override fun getItemCount(): Int = items.size

    class MonthYearViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val tvItem: TextView = itemView.findViewById(R.id.tvItem)
        private val viewSelected: View = itemView.findViewById(R.id.viewSelected)

        fun bind(item: String, isSelected: Boolean, onClick: () -> Unit) {
            tvItem.text = item
            viewSelected.visibility = if (isSelected) View.VISIBLE else View.GONE
            itemView.setOnClickListener { onClick() }
        }
    }
}