"use client"
import Link from 'next/link';
import Avatar from '@/components/Avatar';
import Image from 'next/image';
import AvatarMenu from '@/components/ProfileDropdown';

const Header = () => {
    return (
        <header>
            <nav>
                <Link href="/">
                    <div className="main-logo-section">
                        <div className = "ls-left">
                            <Image 
                                src="/images/Logo.svg" 
                                alt="ISIT GAMING Logo" 
                                width = {80}
                                height ={80}
                                margin-right = {15}
                                className='logo'
                            />
                        </div>
                        <div className = "ls-right">
                            <span className="logo-large">ISIT</span><span className="logo-small">.GAMING</span>
                        </div>
                    </div>
                </Link>
                <ul className="Menu">
                    <li><Link href="/tournaments" className="Tournaments">Турниры</Link></li>
                    <li><Link href="/teams" className="Teams">Команды</Link></li>
                    <li><Link href="/rating" className="Rating">Рейтинг</Link></li>
                </ul>
                <AvatarMenu/>
            </nav>
        </header>
    )
}

export default Header