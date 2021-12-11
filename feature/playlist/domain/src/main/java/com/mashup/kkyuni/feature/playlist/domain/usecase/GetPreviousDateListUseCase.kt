package com.mashup.kkyuni.feature.playlist.domain.usecase

import com.mashup.kkyuni.feature.playlist.domain.model.ChoiceDate
import com.mashup.kkyuni.feature.playlist.domain.model.Date
import kotlinx.coroutines.flow.flow
import java.util.*
import javax.inject.Inject

class GetPreviousDateListUseCase @Inject constructor() {

    operator fun invoke(year: Int, month: Int) = flow {
        val dates = mutableListOf<ChoiceDate>()

        val calendar = Calendar.getInstance().apply {
            set(Calendar.YEAR, year)
            set(Calendar.MONTH, month)

            add(Calendar.YEAR, -1)
        }

        repeat(ITEM_COUNT){
            if(calendar.get(Calendar.YEAR) < 2021
                || (calendar.get(Calendar.YEAR) == 2021
                    && calendar.get(Calendar.MONTH) < 9)
            ){
                // 캘린더영역에 start date가 2021년 9월로 고정되어 있어
                // 추가 해주지 않는다. 추후 개선시 해당 조건문 제거
                if(calendar.get(Calendar.YEAR) == 2021
                    && (calendar.get(Calendar.MONTH) == 7
                        || calendar.get(Calendar.MONTH) == 8)){
                    dates.add(
                        ChoiceDate(
                            Date(-1, -1)
                        )
                    )
                }
            }else {
                dates.add(
                    ChoiceDate(
                        Date(
                            if(calendar.get(Calendar.MONTH) == 0){
                                calendar.get(Calendar.YEAR) -1
                            }else {
                                calendar.get(Calendar.YEAR)
                            }
                            ,
                            if(calendar.get(Calendar.MONTH) == 0){
                                12
                            }else {
                                calendar.get(Calendar.MONTH)
                            }
                        )
                    )
                )
            }

            calendar.add(Calendar.MONTH, 1)
        }

        emit(dates)
    }

    companion object {
        const val ITEM_COUNT = 12
    }
}
