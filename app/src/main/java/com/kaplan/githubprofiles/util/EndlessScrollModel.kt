package com.kaplan.githubprofiles.util

import androidx.databinding.BaseObservable
import androidx.databinding.Bindable
import com.kaplan.githubprofiles.BR

class EndlessScrollModel : BaseObservable() {

  @get:Bindable
  var visibleThreshold: Int = 0
    set(visibleThreshold) {
      field = visibleThreshold
      notifyPropertyChanged(BR.visibleThreshold)
    }

  @get:Bindable
  var currentPage: Int = 0
    set(visibleThreshold) {
      field = visibleThreshold
      notifyPropertyChanged(BR.visibleThreshold)
    }
}
