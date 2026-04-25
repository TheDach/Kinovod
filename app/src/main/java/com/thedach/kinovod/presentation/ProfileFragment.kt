package com.thedach.kinovod.presentation

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.squareup.picasso.Picasso
import com.thedach.kinovod.R
import com.thedach.kinovod.data.repository.UserRepository
import com.thedach.kinovod.databinding.FragmentProfileBinding
import com.thedach.kinovod.databinding.ItemFriendBinding
import com.thedach.kinovod.databinding.ItemMovieBinding
import com.thedach.kinovod.domain.model.Friend
import com.thedach.kinovod.domain.model.Movie
import com.thedach.kinovod.domain.model.User

class ProfileFragment : Fragment() {

    private var _binding: FragmentProfileBinding? = null
    private val binding: FragmentProfileBinding
        get() = _binding ?: throw RuntimeException("FragmentProfileBinding == null")

    private val wishlistMovieBindings = mutableListOf<ItemMovieBinding>()
    private val watchedMovieBindings = mutableListOf<ItemMovieBinding>()
    private val friendBindings = mutableListOf<ItemFriendBinding>()

    private val viewModel: ProfileViewModel by lazy {
        ViewModelProvider(this)[ProfileViewModel::class.java]
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentProfileBinding.inflate(inflater, container, false)
        initIncludeBindings()
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupObserver()
        setupClickListeners()
    }

    private fun initIncludeBindings() {
        with(binding) {
            // Wishlist movies
            wishlistMovieBindings.addAll(
                listOf(
                    ItemMovieBinding.bind(itemMovieWatchlist1Profile.root),
                    ItemMovieBinding.bind(itemMovieWatchlist2Profile.root),
                    ItemMovieBinding.bind(itemMovieWatchlist3Profile.root),
                    ItemMovieBinding.bind(itemMovieWatchlist4Profile.root),
                    ItemMovieBinding.bind(itemMovieWatchlist5Profile.root)
                )
            )

            // Watched movies
            watchedMovieBindings.addAll(
                listOf(
                    ItemMovieBinding.bind(movieWatched1Profile.root),
                    ItemMovieBinding.bind(movieWatched2Profile.root),
                    ItemMovieBinding.bind(movieWatched3Profile.root),
                    ItemMovieBinding.bind(movieWatched4Profile.root),
                    ItemMovieBinding.bind(movieWatched5Profile.root)
                )
            )

            // Friends
            friendBindings.addAll(
                listOf(
                    ItemFriendBinding.bind(friendItem1Profile.root),
                    ItemFriendBinding.bind(friendItem2Profile.root),
                    ItemFriendBinding.bind(friendItem3Profile.root)
                )
            )
        }
    }

    private fun setupObserver() {
        UserRepository.currentUser.observe(viewLifecycleOwner) { user ->
            bindViewsUser(user)
            viewModel.refreshMovies()
        }
        viewModel.movieWishList.observe(viewLifecycleOwner) { wishList ->
            displayWishlist(wishList)
        }
        viewModel.movieWatchedList.observe(viewLifecycleOwner) { watchedList ->
            displayWatchedList(watchedList)
        }
        viewModel.error.observe(viewLifecycleOwner) {error ->
            if (!error.isNullOrBlank()) {
                Toast.makeText(requireContext(), error, Toast.LENGTH_SHORT).show()
            }
        }
    }
    private fun setupClickListeners() {
        wishlistMovieBindings.forEachIndexed { index, binding ->
            binding.root.setOnClickListener {
                val movie = viewModel.movieWishList.value?.getOrNull(index)
                movie?.let {movie ->
                    launchMovieDetailsFragment(movie)
                }
            }
        }

        // Клики для фильмов в watched
        watchedMovieBindings.forEachIndexed { index, binding ->
            binding.root.setOnClickListener {
                val movie = viewModel.movieWatchedList.value?.getOrNull(index)
                movie?.let {movie ->
                    launchMovieDetailsFragment(movie)
                }
            }
        }

        binding.tvWishlistSeeAll.setOnClickListener {

        }
        binding.tvWatchedSeeAllProfile.setOnClickListener {

        }
        binding.tvFriendsSeeAllProfile.setOnClickListener {

        }
    }

    private fun launchMovieDetailsFragment(movie: Movie) {
        findNavController().navigate(
            ProfileFragmentDirections.actionProfileFragmentToMovieDetailsFragment(movie)
        )
    }

    private fun bindViewsUser(user: User) {
        with(binding) {
            // Основная информация
            tvUserNameProfile.text = user.username
            tvUserUsernameProfile.text = getString(R.string.tv_username_and_date)
                .format(user.userTag, "апреля", "2026")

            // Статистика
            tvFriendsCountProfile.text = user.friends?.size?.toString() ?: "0"
            tvWatchedCountProfile.text = user.watchedList?.size?.toString() ?: "0"
            tvWishlistCountProfile.text = user.wishList?.size?.toString() ?: "0"

            initAvatar(user)

            // Текст "Все X"
            tvFriendsSeeAllProfile.text = getString(R.string.tv_all_watched_list)
                .format(user.friends?.size?.toString())
            tvWatchedSeeAllProfile.text = getString(R.string.tv_all_watched_list)
                .format(user.watchedList?.size?.toString())
            tvWishlistSeeAll.text = getString(R.string.tv_all_watched_list)
                .format(user.wishList?.size?.toString())

            wishlistSection.isVisible = !user.wishList.isNullOrEmpty()
            watchedSection.isVisible = !user.watchedList.isNullOrEmpty()
            friendsSection.isVisible = !user.friends.isNullOrEmpty()


            if (!user.friends.isNullOrEmpty()) {
                displayFriends(user.friends)
            }
        }
    }

    private fun displayWishlist(wishlist: List<Movie>) {
        // Берем последние 5 элементов
        val last5Items = wishlist.takeLast(5)

        // Скрываем все элементы
        wishlistMovieBindings.forEach { it.root.isVisible = false }

        // Показываем нужное количество
        last5Items.forEachIndexed { index, movie ->
            if (index < wishlistMovieBindings.size) {
                val binding = wishlistMovieBindings[index]
                binding.root.isVisible = true
                bindMovieData(binding, movie)
            }
        }
    }
    private fun displayWatchedList(watchedList: List<Movie>) {
        // Берем последние 5 элементов
        val last5Items = watchedList.takeLast(5)

        // Скрываем все элементы
        watchedMovieBindings.forEach { it.root.isVisible = false }

        // Показываем нужное количество
        last5Items.forEachIndexed { index, movie ->
            if (index < watchedMovieBindings.size) {
                val binding = watchedMovieBindings[index]
                binding.root.isVisible = true
                bindMovieData(binding, movie)
            }
        }
    }
    private fun displayFriends(friends: List<Friend>) {
        // Берем последние 3 элемента
        val last3Items = friends.takeLast(3)

        // Скрываем все элементы
        friendBindings.forEach { it.root.isVisible = false }

        // Показываем нужное количество
        last3Items.forEachIndexed { index, friend ->
            if (index < friendBindings.size) {
                val binding = friendBindings[index]
                binding.root.isVisible = true
                bindFriendData(binding, friend)
            }
        }
    }

    private fun bindMovieData(binding: ItemMovieBinding, movie: Movie) {
        with(binding) {
            tvMovieName.text = movie.name
            tvMovieTag.text = movie.genres[0]

            tvMovieTime.text = getString(R.string.tag_and_time_poster)
                .format(movie.movieLengthHour, movie.movieLengthMin)
            tvRatingKinopoisk.text = getString(R.string.rating_poster_kinopoinsk_poster)
                .format(movie.rating.kp)
            tvRatingImdb.text = getString(R.string.rating_poster_IMDb_poster)
                .format(movie.rating.imdb)
            tvRatingStarPoster.text = getString(R.string.rating_star_poster)
                .format(movie.rating.kp)

            Picasso.get().load(movie.poster).into(imageViewMoviePoster)
        }
    }
    private fun bindFriendData(binding: ItemFriendBinding, friend: Friend) {
        binding.tvFriendName.text = friend.username

        // Загрузка реального аватара если есть
        if (!friend.avatar.isNullOrEmpty()) {
            // Добавить элемент imageView
            /*Picasso.get().load(friend.avatar).into(binding.text1)*/
        } else {
            binding.friendAvatar.setCardBackgroundColor(
                androidx.core.content.ContextCompat.getColor(
                    requireContext(),
                    R.color.purple_500
                ) // TODO("менять цвета пользователей")
            )
            // Отображаем первую букву имени как аватар
            binding.tvFriendAvatar.text = friend.username.take(1).uppercase()
        }
    }

    private fun initAvatar(user: User) {
        with(binding) {
            if (!user.avatar.isNullOrEmpty()) {
                TODO()
            } else {
                setDefaultAvatar(user.username)
            }
        }
    }
    private fun setDefaultAvatar(username: String) {
        with(binding) {
            tvAvatarProfile.isVisible = true
            tvAvatarProfile.text = username.take(1).uppercase()
            cardViewAvatarColorProfile.setCardBackgroundColor(
                androidx.core.content.ContextCompat.getColor(
                    requireContext(),
                    R.color.purple_500
                ) // TODO("менять цвета пользователей")
            )
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()

        wishlistMovieBindings.clear()
        watchedMovieBindings.clear()
        friendBindings.clear()

        _binding = null

    }
}