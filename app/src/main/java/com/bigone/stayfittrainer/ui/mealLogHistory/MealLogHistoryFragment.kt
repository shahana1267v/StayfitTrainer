package com.bigOne.stayfittrainer.ui.mealLogHistory

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.AppCompatTextView
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.bigOne.stayfittrainer.R
import com.bigOne.stayfittrainer.databinding.FragmentLogHistoryMealBinding
import com.bigOne.stayfittrainer.datas.model.SavedFood
import com.bigOne.stayfittrainer.ui.meal.MealTypeFragment
import com.bigOne.stayfittrainer.ui.meal.MealViewModel
import com.bigOne.stayfittrainer.ui.meal.SelectedFoodAdapter
import com.bigOne.stayfittrainer.utils.DateUtils
import com.michalsvec.singlerowcalendar.calendar.CalendarChangesObserver
import com.michalsvec.singlerowcalendar.calendar.CalendarViewManager
import com.michalsvec.singlerowcalendar.calendar.SingleRowCalendarAdapter
import com.michalsvec.singlerowcalendar.selection.CalendarSelectionManager
import java.time.LocalDate
import java.util.Calendar
import java.util.Date
import java.text.SimpleDateFormat
import java.util.Locale


class MealLogHistoryFragment : Fragment() {
    lateinit var binding: FragmentLogHistoryMealBinding
    private val calendar = Calendar.getInstance()
    private var currentMonth = 0
    var selctedDate: Date? = null
    private lateinit var mAdapterBf: SelectedFoodAdapter
    private lateinit var mAdapterMS: SelectedFoodAdapter
    private lateinit var mAdapterLH: SelectedFoodAdapter
    private lateinit var mAdapterES: SelectedFoodAdapter
    private lateinit var mAdapterDN: SelectedFoodAdapter
    private val historyViewModel: MealHistoryViewModel by activityViewModels()
    private val mealViewModel : MealViewModel by activityViewModels()
    private var datedfoodlist : List<SavedFood> = mutableListOf()
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        // Inflate the layout for this fragment
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_log_history_meal, container, false)
        return binding.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        calendar.time = Date()
        currentMonth = calendar[Calendar.MONTH]
        init()
        initAdapter()
        observer()
    }


    private fun initAdapter() {
        binding.apply {
            breakfastList.layoutManager = LinearLayoutManager(context)
            breakfastList.setHasFixedSize(true)
            mAdapterBf = SelectedFoodAdapter(mutableListOf(),requireContext())
            breakfastList.adapter = mAdapterBf

            mrngsnacklist.layoutManager = LinearLayoutManager(context)
            mrngsnacklist.setHasFixedSize(true)
            mAdapterMS = SelectedFoodAdapter(mutableListOf(),requireContext())
            mrngsnacklist.adapter = mAdapterMS

            lunchlist.layoutManager = LinearLayoutManager(context)
            lunchlist.setHasFixedSize(true)
            mAdapterLH = SelectedFoodAdapter(mutableListOf(),requireContext())
            lunchlist.adapter = mAdapterLH

            evngSnacklist.layoutManager = LinearLayoutManager(context)
            evngSnacklist.setHasFixedSize(true)
            mAdapterES = SelectedFoodAdapter(mutableListOf(),requireContext())
            evngSnacklist.adapter = mAdapterES

            dinnerlist.layoutManager = LinearLayoutManager(context)
            dinnerlist.setHasFixedSize(true)
            mAdapterDN = SelectedFoodAdapter(mutableListOf(),requireContext())
            dinnerlist.adapter = mAdapterDN



        }




    }
    private fun observer() {

       }

    private fun init() {
        val currentDate = LocalDate.now()
        // Get the day of the month from the current date
        val dayOfMonth = currentDate.dayOfMonth-1
        val currentMonth = currentDate.monthValue
        val currentYear = currentDate.year
        val calendar = Calendar.getInstance()
        calendar.set(currentYear, currentMonth - 1, 1)
        val singleRowCalendar = binding.datePickerTimeline.apply {
            calendarViewManager = myCalendarViewManager
            calendarChangesObserver = myCalendarChangesObserver
            calendarSelectionManager = mySelectionManager
            setDates(getFutureDatesOfCurrentMonth())
            init()
           select(dayOfMonth)
        }
       singleRowCalendar.scrollToPosition(dayOfMonth)
        binding.apply {
            btnRight.setOnClickListener {
                calendar.add(Calendar.MONTH, 1)
                singleRowCalendar.setDates(getDatesOfNextMonth())
                singleRowCalendar.init()
                updateToolbarMonth(calendar)
            }

            btnLeft.setOnClickListener {
                calendar.add(Calendar.MONTH, -1)
                singleRowCalendar.setDates(getDatesOfPreviousMonth())
                singleRowCalendar.init()
                updateToolbarMonth(calendar)
            }
        }
        binding.toolbar.setNavigationOnClickListener {
            findNavController().popBackStack()
        }

        binding.brkFstLogMealBtn.setOnClickListener {
             val groupedFoods = datedfoodlist.groupBy { it.mealType }
            Log.e("foodsGrp",groupedFoods.toString())

            // Retrieve separately
            groupedFoods[MealTypeFragment.BF]?.let { it1 ->mealViewModel.setfood(it1  ) }
            val action = MealLogHistoryFragmentDirections.actionMealLogHistoryFragmentToMealLogListSaveFragment()
            findNavController().navigate(action)
        }

        binding.mrngSnackBtn.setOnClickListener {
            val groupedFoods = datedfoodlist.groupBy { it.mealType }
            Log.e("foodsGrp",groupedFoods.toString())

            // Retrieve separately
            groupedFoods[MealTypeFragment.MS]?.let { it1 ->mealViewModel.setfood(it1  ) }
            val action = MealLogHistoryFragmentDirections.actionMealLogHistoryFragmentToMealLogListSaveFragment()
            findNavController().navigate(action)
        }


        binding.lunchBtn.setOnClickListener {
            val groupedFoods = datedfoodlist.groupBy { it.mealType }
            Log.e("foodsGrp",groupedFoods.toString())

            // Retrieve separately
            groupedFoods[MealTypeFragment.LH]?.let { it1 ->mealViewModel.setfood(it1  ) }
            val action = MealLogHistoryFragmentDirections.actionMealLogHistoryFragmentToMealLogListSaveFragment()
            findNavController().navigate(action)
        }

        binding.evningSnackBtn.setOnClickListener {
            val groupedFoods = datedfoodlist.groupBy { it.mealType }
            Log.e("foodsGrp",groupedFoods.toString())

            // Retrieve separately
            groupedFoods[MealTypeFragment.ES]?.let { it1 ->mealViewModel.setfood(it1  ) }
            val action = MealLogHistoryFragmentDirections.actionMealLogHistoryFragmentToMealLogListSaveFragment()
            findNavController().navigate(action)
        }
        binding.dinnerBtn.setOnClickListener {
            val groupedFoods = datedfoodlist.groupBy { it.mealType }
            Log.e("foodsGrp",groupedFoods.toString())

            // Retrieve separately
            groupedFoods[MealTypeFragment.DN]?.let { it1 ->mealViewModel.setfood(it1  ) }
            val action = MealLogHistoryFragmentDirections.actionMealLogHistoryFragmentToMealLogListSaveFragment()
            findNavController().navigate(action)
        }


    }

    private fun updateToolbarMonth(calendar: Calendar) {
        val monthName = SimpleDateFormat("MMMM", Locale.getDefault()).format(calendar.time)
        val year = calendar.get(Calendar.YEAR)
        binding.tvDate.text = "$monthName, $year" // Update toolbar text with current month and year
    }

            val myCalendarViewManager = object : CalendarViewManager {
                override fun setCalendarViewResourceId(position: Int, date: Date, isSelected: Boolean): Int {
                    return if(isSelected)
                        R.layout.selected_calendar
                    else{
                        R.layout.un_selected_calendar
                    }
                }
                override fun bindDataToCalendarView(
                    holder: SingleRowCalendarAdapter.CalendarViewHolder,
                    date: Date,
                    position: Int,
                    isSelected: Boolean
                ) {
                    holder.itemView.findViewById<AppCompatTextView>(R.id.tv_date_calendar_item).text = DateUtils.getDayNumber(date)
                    holder.itemView.findViewById<AppCompatTextView>(R.id.tv_day_calendar_item).text = DateUtils.getDay3LettersName(date)
                }
            }

        val mySelectionManager = object : CalendarSelectionManager {
            override fun canBeItemSelected(position: Int, date: Date): Boolean {
                Log.e("date",date.toString())
                callFoodData(date)
               return  true
            }
        }

    private fun callFoodData(date: Date) {
       // clearAllData()
            historyViewModel.getSelectedDateFoodList(date).observe(viewLifecycleOwner){
                datedfoodlist=it

                Log.e("foods",it.toString())
                if(!it.isNullOrEmpty()){
                    // Group foods by meal type
                    val groupedFoods = it.groupBy { it.mealType }
                    Log.e("foodsGrp",groupedFoods.toString())

                    Log.e("evng", (groupedFoods[4] ?: " ").toString())
                    // Retrieve separately
                    groupedFoods[MealTypeFragment.BF]?.let { it1 -> mAdapterBf.setItems(it1) }
                    groupedFoods[MealTypeFragment.MS]?.let { it1 -> mAdapterMS.setItems(it1) }
                    groupedFoods[MealTypeFragment.LH]?.let { it1 -> mAdapterLH.setItems(it1) }
                    groupedFoods[MealTypeFragment.ES]?.let { it1 -> mAdapterES.setItems(it1) }
                    groupedFoods[MealTypeFragment.DN]?.let { it1 -> mAdapterDN.setItems(it1) }
                }else{
                    clearAllData()
                }
            }
    }

    private fun clearAllData() {
        mAdapterBf.clearItems()
        mAdapterMS.clearItems()
        mAdapterLH.clearItems()
        mAdapterES.clearItems()
        mAdapterDN.clearItems()
    }

    val myCalendarChangesObserver = object : CalendarChangesObserver {
            override fun whenWeekMonthYearChanged(weekNumber: String,monthNumber: String,monthName: String,year: String,date: Date) {
                super.whenWeekMonthYearChanged(weekNumber, monthNumber, monthName, year, date)
            }

            override fun whenSelectionChanged(isSelected: Boolean, position: Int, date: Date) {
                selctedDate =date
                binding.apply {
                    tvDate.text = "${DateUtils.getMonthName(date)}, ${DateUtils.getDayNumber(date)} "
                    tvDay.text = DateUtils.getDayName(date)
                }
                super.whenSelectionChanged(isSelected, position, date)
            }

            override fun whenCalendarScrolled(dx: Int, dy: Int) {
                super.whenCalendarScrolled(dx, dy)
            }

            override fun whenSelectionRestored() {
                super.whenSelectionRestored()
            }

            override fun whenSelectionRefreshed() {
                super.whenSelectionRefreshed()
            }
        }
    private fun getDates(calendar: Calendar): List<Date> {
        val list = mutableListOf<Date>()
        val currentMonth = calendar.get(Calendar.MONTH)
        calendar.set(Calendar.DAY_OF_MONTH, 1)
        list.add(calendar.time)
        while (calendar.get(Calendar.MONTH) == currentMonth) {
            calendar.add(Calendar.DATE, 1)
            if (calendar.get(Calendar.MONTH) == currentMonth)
                list.add(calendar.time)
        }
        calendar.add(Calendar.DATE, -1)
        return list
    }

    private fun getFutureDatesOfCurrentMonth(calendar: Calendar): List<Date> {
        val list = mutableListOf<Date>()
        val currentMonth = calendar.get(Calendar.MONTH)
        while (calendar.get(Calendar.MONTH) == currentMonth) {
            list.add(calendar.time)
            calendar.add(Calendar.DATE, 1)
        }
        return list
    }

    private fun getDatesOfNextMonth(): List<Date> {
        currentMonth++ // + because we want next month
        if (currentMonth == 12) {
            // we will switch to january of next year, when we reach last month of year
            calendar.set(Calendar.YEAR, calendar[Calendar.YEAR] + 1)
            currentMonth = 0 // 0 == january
        }
        return getDates(mutableListOf())
    }

    private fun getDatesOfPreviousMonth(): List<Date> {
        currentMonth-- // - because we want previous month
        if (currentMonth == -1) {
            // we will switch to december of previous year, when we reach first month of year
            calendar.set(Calendar.YEAR, calendar[Calendar.YEAR] - 1)
            currentMonth = 11 // 11 == december
        }
        return getDates(mutableListOf())
    }

    private fun getFutureDatesOfCurrentMonth(): List<Date> {
        // get all next dates of current month
        currentMonth = calendar[Calendar.MONTH]
        return getDates(mutableListOf())
    }


    private fun getDates(list: MutableList<Date>): List<Date> {
        // load dates of whole month
        calendar.set(Calendar.MONTH, currentMonth)
        calendar.set(Calendar.DAY_OF_MONTH, 1)
        list.add(calendar.time)
        while (currentMonth == calendar[Calendar.MONTH]) {
            calendar.add(Calendar.DATE, +1)
            if (calendar[Calendar.MONTH] == currentMonth)
                list.add(calendar.time)
        }
        calendar.add(Calendar.DATE, -1)
        return list
    }



}