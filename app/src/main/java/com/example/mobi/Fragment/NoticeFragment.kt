package com.example.mobi.Fragment

import android.app.ListActivity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentTransaction
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.mobi.Activity.AddArticleActivity
import com.example.mobi.Activity.MainActivity
import com.example.mobi.Activity.NoticeDetailActivity
import com.example.mobi.Adapter.ArticleAdapter
import com.example.mobi.ArticleModel
import com.example.mobi.DBkey.Companion.DB_ARTICLES
import com.example.mobi.R
import com.example.mobi.databinding.FragmentNoticeBinding
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.ChildEventListener
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import kotlinx.android.synthetic.main.activity_calendar.*
import kotlinx.android.synthetic.main.activity_calendar.view.*
import kotlinx.android.synthetic.main.fragment_notice.*
import kotlinx.android.synthetic.main.item_article.*
import kotlinx.android.synthetic.main.item_article.view.*
import java.util.*


class NoticeFragment : Fragment(R.layout.fragment_notice) {

    private lateinit var articleAdapter: ArticleAdapter
    private lateinit var articleDB: DatabaseReference
    private val articleList = mutableListOf<ArticleModel>() // 데이터 스냅샷을 통한 데이터 변경을 알기위해 아티클데이터 변수 설정
    private var article : ArrayList<ArticleModel> = arrayListOf()
    //차일드 이벤트 리스너 전역변수 설정
    private val listener = object : ChildEventListener {
        override fun onChildAdded(snapshot: DataSnapshot, previousChildName: String?) {
            //모델클래스 자체를 업로드하고 다운받는다
            val articleModel = snapshot.getValue(ArticleModel::class.java)
            articleModel ?: return //모델이 없을 경우 리턴

            articleList.add(articleModel) //아티클모텔을 리스트에 추가
            articleAdapter.submitList(articleList) //아티클 어뎁터에 리스트 넣어주기

        }

        override fun onChildChanged(snapshot: DataSnapshot, previousChildName: String?) {
        }

        override fun onChildRemoved(snapshot: DataSnapshot) {
        }

        override fun onChildMoved(snapshot: DataSnapshot, previousChildName: String?) {

        }

        override fun onCancelled(error: DatabaseError) {}

    }

    private var binding: FragmentNoticeBinding? = null
    private val auth: FirebaseAuth by lazy {
        Firebase.auth
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        val fragmentNoticeBinding = FragmentNoticeBinding.bind(view)
        binding = fragmentNoticeBinding

        articleList.clear() // 화면 옮기면 두번 출력되는거 방지

        articleDB = Firebase.database.reference.child(DB_ARTICLES) //아티클DB firebase 데이터베이스 선언
        articleAdapter = ArticleAdapter(itemClickedlistener = {
            val intent = Intent(context, NoticeDetailActivity::class.java)
            intent.flags =Intent.FLAG_ACTIVITY_CLEAR_TASK
            intent.putExtra("title", it.title )
            intent.putExtra("contents",it.contents )
            intent.putExtra("photo", it.imageUrl)
            intent.putExtra("date", it.createdAt)
            context?.startActivity(intent)

        }) //아티클 어댑터 호출

        articleDB.addChildEventListener(listener) //아티클 데이터 이벤트 리스너에 추가


        fragmentNoticeBinding.articleRecyclerView.layoutManager = LinearLayoutManager(context)
        fragmentNoticeBinding.articleRecyclerView.adapter = articleAdapter

        (fragmentNoticeBinding.articleRecyclerView.layoutManager as LinearLayoutManager).reverseLayout =
            true
        (fragmentNoticeBinding.articleRecyclerView.layoutManager as LinearLayoutManager)
            .stackFromEnd = true
        articleRecyclerView.layoutManager = (fragmentNoticeBinding.articleRecyclerView.layoutManager as LinearLayoutManager)


        //플로팅버튼 fragment에서 화면 넘기기
        fragmentNoticeBinding.addFloatingButton.setOnClickListener {
            context?.let {

                //회원만 게시글을 올릴 수 있게 해놨다. TODO: 지정한 운영자만 게시글을 올릴 수 있게 수정하기
                if (auth.currentUser != null  ) {
                    val intent = Intent(requireContext(), AddArticleActivity::class.java)
                    startActivity(intent)
                } else {
                    Snackbar.make(view, "운영자만 사용 가능합니다", Snackbar.LENGTH_LONG).show()
                }


            }
        }
        //  articleDB.addChildEventListener(listener)
    }

    fun refreshFragment(fragment: Fragment, fragmentManager: FragmentManager) {
        var ft: FragmentTransaction = fragmentManager.beginTransaction()
        ft.detach(fragment).attach(fragment).commit()
    }
    //  게시글 올렸을때 데이터 변환을 알고 새로고침해줌
    override fun onResume() {
        super.onResume()


//        articleList.clear() // 화면 옮기면 두번 출력되는거 방지
        getFragmentManager()?.let { refreshFragment(this, it) }
        //articleAdapter.notifyDataSetChanged()
    }

    override fun onDestroyView() {
        super.onDestroyView()

        articleDB.removeEventListener(listener)
    }

}